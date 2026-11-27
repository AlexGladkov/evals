# A5 Report: Offline-first (cache-then-network) via Room

## Caching strategy and why

**Network-first with Room-cache fallback** (a cache-then-network variant adapted to the
project's `Single`-based RxJava2 API):

1. Every fetch hits the network first — **online behavior is byte-for-byte unchanged**
   (same requests, same converters, same income math; the math was only extracted into a
   private `computeIncome(result)` function, not modified).
2. On network success, the raw API payload is serialized (Gson) and written to a Room
   cache table (`doOnSuccess { cacheStats(...) }`).
3. On network failure, `onErrorResumeNext` serves the Room cache (deserialized and run
   through the *same* converters/computation, so cached results are identical in shape to
   online results).
4. If there is neither network nor cache, the original network error propagates and the
   screen shows an offline/empty state instead of crashing or rendering fake data.

The spec's own note blesses this shape for `Single` APIs: since a `Single` emits at most
once, we cannot push "cached, then network" into one subscription without rewriting the
domain interfaces and all presenters. Network-first keeps online fidelity guaranteed and
makes the cache a pure availability fallback. `cachedAt` freshness metadata is preserved
(`Income.cachedAt` is populated when data comes from cache, `null` when fresh), so
presenters can render a "Data from <time>" indicator.

## What is cached, granularity

Three new Room tables, keyed **per miner address** (`@PrimaryKey miner`), one row per
data type (REPLACE on conflict → always the freshest snapshot wins):

| Table | Entity | Payload (Gson JSON) | Served by |
|---|---|---|---|
| `cached_stats` | `CachedStatsEntity` | `ApiProviderEx` | `MoneyRepositoryImpl.fetchMinerIncome` / `fetchUnpaidMoney` |
| `cached_workers` | `CachedWorkersEntity` | `List<ApiWorker>` | `MinerRepositoryImpl.fetchWorkers` |
| `cached_payouts` | `CachedPayoutsEntity` | `List<ApiPayment>` | `MinerRepositoryImpl.fetchMinerPayments` |

Each row stores `miner`, `data` (JSON), `cachedAt` (epoch millis). Exchange rates were
already cached in Room (`ExchangeRateEntity`) and are untouched. DB version bumped
10 → 11 (destructive migration, matching the app's existing `fallbackToDestructiveMigration`
policy). New DAOs: `CachedStatsDao`, `CachedWorkersDao`, `CachedPayoutsDao`
(`getByMiner` / `save` / `clear`), exposed through `RoomModule` → `RepositoryModule`.
`MinerRepositoryImpl` now depends on `MinerDao` + the cache DAOs instead of the concrete
`RoomAppDataSource` (testability, behavior unchanged).

## Network recovery mechanism and why

**Self-healing on each request** — no connectivity observer. Every fetch attempts the
network first; any success overwrites the cache. Consequences:

- When connectivity returns, the very next screen open refreshes the cache and the UI.
- While offline, the stale cache keeps being served (UI keeps working with last-known data).
- No background polling/broadcast receivers — simplest mechanism that satisfies
  "recovery works", and it never shows stale data when fresh data is reachable.

## Offline + no cache handling

Repository propagates the original network error. `IncomePresenter` (previously swallowed
errors with `printStackTrace`) now calls the new `IncomeView.setupOffline()`, implemented
in `IncomeFragment` with a toast ("Offline — no cached data yet. Reconnect to refresh.").
Workers/Payouts screens already had `showError` handling and needed no changes.

## Tests

Plain JVM tests (fake providers implementing `NicehashProvider`/`MinerProvider`, fake
in-memory DAOs implementing the Room DAO interfaces, real Gson/converters — no
Robolectric needed):

- `MoneyRepositoryImplTest` (8): online income+unpaid fetched **and** cached; offline with
  cache serves identical values + `cachedAt` freshness; offline without cache → error;
  per-miner granularity; recovery (online after offline refreshes cache).
- `MinerRepositoryImplTest` (8): workers/payouts online-fetched+cached, offline-from-cache,
  offline-no-cache error, recovery updates cache, plus a CRUD regression check.

`./gradlew test` green (all variants/modules), `./gradlew assembleDebug` builds.
