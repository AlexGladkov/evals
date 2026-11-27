# A2 — Architectural Audit Report: SOLID/DRY Violations Found & Fixed

**Project:** `source/nicehash` (Android, Kotlin) · **Branch:** `task/a2`
**Constraint honored:** app behavior is bit-for-bit identical (only comments, structure and symbols changed; no calculation touched).

Legend: ✅ = fixed, ⚠️ = found & documented, deliberately not fixed (спорное / would change behavior / separate task).

---

## Fixed violations

### 1. Code-organization violation: class at wrong package level ✅
- **Where:** `app/src/main/java/com/dev/nicehash/HeavyClass.kt` (the only class at the root package, outside `app/`, `helpers/`, etc.)
- **Principle:** code organization / package structure.
- **Fix:** moved to `helpers` package (`helpers/HeavyClass.kt`, `package com.dev.nicehash.helpers`). No references existed, so nothing else changed.

### 2. SRP violation: `MoneyRepositoryImpl` mixes network, calculation and time logic ✅
- **Where:** `data/implementations/MoneyRepositoryImpl.kt`
- **Principle:** SRP (Single Responsibility). The repository fetched remote stats, computed per-algorithm profitability, aggregated a time chart, applied a day window (`minusDays(1)`) and derived hour/day/week/month income — all in one class.
- **Fix:** extracted the pure computation verbatim into a new dedicated class `data/implementations/IncomeCalculator.kt` (`calculate(result: ApiProviderEx): Income`). `MoneyRepositoryImpl` now only orchestrates the provider call and delegates to the calculator. Identical math, identical inputs/outputs.

### 3. DRY violation: two separately-configured Retrofit builders in `RemoteModule` ✅
- **Where:** `app/di/RemoteModule.kt`
- **Principle:** DRY. `provideRetrofit` and `provideRemoteCurrencyService` duplicated the full Retrofit builder chain (converter + call adapter + client).
- **Fix:** single private `buildRetrofit(baseUrl, gson, okHttpClient)` used by both providers. Same settings, same base URLs → identical behavior.

### 4. Hardcoded configuration: blockchain.info URL inline in DI module ✅
- **Where:** `app/di/RemoteModule.kt` (`"https://blockchain.info/"` inline)
- **Principle:** OCP / configuration-vs-code separation (category "hardcoded configs"). `RemoteContract` already exists as the designated place for endpoints.
- **Fix:** added `RemoteContract.baseUrlCurrency`; `RemoteModule` references the contract.

### 5. Hardcoded configuration: NiceHash API method names scattered as string literals ✅
- **Where:** `data/providers/services/RemoteMinerService.kt` (query defaults), `data/providers/implementations/MinerProviderImpl.kt` (`"stats.provider.ex"`, `"stats.provider"`, `"stats.provider.workers"`)
- **Principle:** OCP / hardcoded strings; `RemoteContract` documents itself as the place for "endpoints, params".
- **Fix:** added `METHOD_STATS_PROVIDER`, `METHOD_STATS_PROVIDER_EX`, `METHOD_STATS_PROVIDER_WORKERS`, `METHOD_STATS_PROVIDER_PAYMENTS` to `RemoteContract` and replaced the literals. Same strings are sent over the wire.

### 6. Hardcoded configuration: demo wallet address inline ✅
- **Where:** `data/implementations/MinerRepositoryImpl.kt` (`"3Aqj4o27ixprba67GvBctDkXijxxpbD4hS"` inline in `fetchMinerStatistics`)
- **Principle:** hardcoded configuration / magic string.
- **Fix:** named companion constant `DEMO_MINER_ADDRESS` with the identical value.

### 7. DIP violation: UI/presenters depend on the concrete `ExchangeServerImpl` via its nested enum ✅
- **Where:** nested `public enum class ExchangeCode` inside `app/servers/ExchangeServerImpl.kt`, referenced by `IncomeFragment`, `BalanceFragment`, `IncomePresenter`, `BalancePresenter`, `PayoutsPresenter`.
- **Principle:** DIP — high-level (UI/presenters) modules referenced a low-level concrete implementation class only to get to the enum.
- **Fix:** extracted the enum to a top-level `app/servers/ExchangeCode.kt` (same package, same values `Ru("RUB")`, `US("USD")`) and updated all references; removed the now-dead `ExchangeServerImpl` imports in `MainActivity` and `AppModule`.

### 8. Hidden global state: static mutable cache in `ExchangeServerImpl` ✅
- **Where:** `companion object { private val data = ArrayList<ExchangeRate>() }` in `app/servers/ExchangeServerImpl.kt`
- **Principle:** encapsulation / testability (static mutable state shared across instances).
- **Fix:** moved the cache to an instance field. The class is an `@Singleton` in the DI graph, so runtime behavior is unchanged; the `-1f` missing-rate sentinel became a named constant `RATE_NOT_FOUND` (same value).

### 9. DRY/OCP violation: theme knowledge duplicated across activities ✅
- **Where:** the `when(App.theme) → setTheme(...)` chain duplicated in `base/BaseActivity.kt` and `app/activities/ThemesActivity.kt`; the dark-theme list duplicated in `BaseActivity` (`App.isDark` computation); the dark/light status-bar-color chain duplicated in `BaseActivity` and `app/activities/ChooseActivity.kt`.
- **Principle:** DRY + OCP (adding a theme required editing 3+ places).
- **Fix:** `enums/Theme.kt` now exposes `isDark`, `styleRes`, `statusBarColorRes` as the single source of truth; `BaseActivity`, `ThemesActivity`, `ChooseActivity` use them. Same style/color resources are selected for every theme value.

### 10. DRY violation: chart fill-drawable `when(App.theme)` duplicated inline ✅
- **Where:** `app/activities/DetailActivity.kt` (`setupChartData`) duplicated the exact mapping already implemented in `helpers/PaintUtils.getChartGradient()`.
- **Principle:** DRY.
- **Fix:** `DetailActivity` now calls `PaintUtils.getChartGradient()`. Same drawable for every theme.

### 11. DRY violation: `getNewInstance` bundle-packing duplicated in 4 fragments ✅
- **Where:** `IncomeFragment`, `BalanceFragment`, `PayoutsFragment`, `WorkersFragment` — identical `Bundle` packing (name/miner/minerID).
- **Principle:** DRY.
- **Fix:** shared generic factory `BaseContainer.getNewInstance<T : BaseContainer>(name, miner, minerID)`; each fragment's companion delegates to it. `MainActivity` call sites unchanged; identical arguments.

### 12. LSP violation: `BaseContainer.getNavigator()` threw `NotImplementedError` ✅
- **Where:** `base/BaseContainer.kt` — `open fun getNavigator(): Navigator? { throw NotImplementedError() }`; every subclass had to override it (all four overrode it with a trivial `return null`), and its only call sites were commented-out code.
- **Principle:** LSP (default member unusable by the base contract) + dead code.
- **Fix:** removed the member entirely together with the four redundant overrides and their unused `navigator` fields / commented bodies. Nothing ever called it live, so behavior is unchanged.

### 13. Dead code: commented-out blocks (category 8) ✅
Commented-out code removed from (bodies kept as empty overrides where the method implements a live interface; live statements preserved):
- `app/fragments/IncomeFragment.kt` — `getNavigator` navigator block, `setupIncome` text-binding block
- `app/fragments/BalanceFragment.kt` — navigator block, adapter-click router call, ListConfig/visibility lines
- `app/fragments/PayoutsFragment.kt` — navigator block, visibility/loading lines, unused ListConfig build
- `app/fragments/WorkersFragment.kt` — same pattern
- `app/activities/MainActivity.kt` — dead `Replace` branches (Detail/Income/Balance/Payouts/Workers), `SystemMessage` comment
- `app/activities/ChooseActivity.kt` — dead theme `when` block, dead click listeners/ListConfig, dead `ScreenKeys.Edit` routing branch
- `app/activities/SettingsActivity.kt` — dead popup-menu Tab/Language routing branches, dead button listeners, dead `setupConfiguration` UI lines
- `app/activities/ThemesActivity.kt` — dead navigator, dead extras-theme selection block, dead click listeners, ENTIRE commented `setupView` body (~250 lines), dead unused private `setStatusBarColor`/`setWindowFlag`
- `app/activities/NotificationsActivity.kt` — dead switch listeners block + unreachable private `onChangeClick` (its only callers were comments)
- `app/activities/PurchasesActivity.kt`, `ServiceStatusActivity.kt`, `CurrenciesActivity.kt` — dead listeners / unused `ListConfig` builds
- `app/activities/SplashActivity.kt` — stale marker comment
- `app/App.kt` — commented Firebase init
- `app/presenters/MainPresenter.kt` — dead routing in `onTabClick`/`onDeviceClick` (methods kept, they are called live)
- `app/presenters/IncomePresenter.kt` — mocked-income block
- `app/presenters/ChoosePresenter.kt` — dead routing in `onMinerClick`, dead `onGearClick`/`onAddClick` (only referenced from removed comments; removed), unused `bundle` in `editClick`
- `app/presenters/SettingsPresenter.kt` — dead `performClick` (only referenced from removed comments; removed)
- `data/providers/implementations/MinerProviderImpl.kt` — ~120 lines of abandoned chart-experiment code; abandoned `getStatistics` body
- `data/implementations/ConfigurationRepositoryImpl.kt` — commented log
- `app/adapters/CurrencyAdapter.kt` — commented removal logic in `performPopularClicked`

### 14. Dead code: unused classes, methods and injections ✅
- `helpers/ImageLoader.kt` — never referenced anywhere → deleted.
- `app/adapters/SwitchAdapter.kt` + `app/models/Switch.kt` — adapter/model pair never instantiated (only a stale import) → deleted (with the stale import in `NotificationsActivity`).
- `helpers/EnumCollections.Algos` (34-entry enum) — never referenced → deleted.
- General-API chain never called: `domain/repositories/GeneralRepository.kt`, `data/implementations/GeneralRepositoryImpl.kt`, `data/providers/GeneralProvider.kt`, `data/providers/implementations/GeneralProviderImpl.kt`, `data/providers/services/RemoteGeneralService.kt` → deleted, plus DI provisions `ProviderModule.createGeneralProvider`, `RepositoryModule.provideGeneralRepository`, `RemoteModule.provideRemoteGeneralService`.
- `CurrencyRepository.updateExchangeRate` — stub (`Single.just(true)`) with zero callers → removed from interface + impl.
- `MinerProvider.getStatistics(miner, time)` — interface method whose only implementation was commented-out code, zero callers → removed from interface + impl.
- `app/di/FirebaseModule.kt` — module whose body was 100% commented (provided nothing) → deleted and removed from `AppComponent`.
- Unused field injections removed: `MainActivity.exchangeServer`, `SplashActivity.generalRepository`, `SplashActivity.exchangeServer` (typo `currencyRepositorry` renamed to `currencyRepository` in the process — private field, no behavior impact).
- Unused `ListConfig` constructions in `ServiceStatusActivity`, `CurrenciesActivity`, `BalanceFragment`, `PayoutsFragment`, `WorkersFragment` (built but never applied, only `applyConfig` was commented) → removed. `ListConfig` itself is still used live by `WorkerHubAdapter`, so it was kept.

### 15. Missing abstraction / readability: inline JSON parsing in `NicehashProviderImpl` ✅
- **Where:** `data/providers/implementations/NicehashProviderImpl.kt` — every provider method carried a large anonymous `flatMap { … gson-parsing … }` lambda.
- **Principle:** missing abstraction (unreadable, untestable parsing embedded in orchestration).
- **Fix:** parsing extracted verbatim into named private methods `parseGlobalCurrent`, `parseGlobal24`, `parseStatsProvider`, `parseStatsProviderEx`, `parseStatsProviderPayments`; the public methods are now one-line `flatMap { parse…(it) }`. Same parse order, same `NullPointerException` error paths, same try/catch skip semantics.

### 16. Copy-paste bug: wrong log tag ✅ (log-only, no user-visible behavior)
- **Where:** `app/activities/SettingsActivity.kt` — `TAG = ChooseActivity::class.java.simpleName`.
- **Fix:** `TAG = SettingsActivity::class.java.simpleName`.

---

## Found but deliberately NOT fixed (спорное / out of scope)

1. **⚠️ `MathUtils.getAverage` divides by zero on empty collections** (`helpers/MathUtils.kt`) — real robustness bug, but it is a **calculation bug**; explicitly out of scope for A2 ("Don't fix calculation bugs"). Not touched.
2. **⚠️ Hardcoded demo data in presenters** — `BalancePresenter.fetchBalance` (demo balances when `id < 0`), `PayoutsPresenter`, `WorkerPresenter`, `CurrencyPresenter.fetchCurrencies` (11-item catalog), `StatusPresenter.fetchStatuses`, `DetailPresenter.loadChartData`, `MainPresenter` fallback entries. Arguably should live in a demo-data provider; extraction is cosmetic (no dependency inversion gained) and risks churn. Marked спорное — left as-is.
3. **⚠️ `ExchangeServerImpl` sentinel `-1f`** for "rate not found" — callers rely on it; changing it changes behavior. Documented via `RATE_NOT_FOUND` constant instead.
4. **⚠️ `ExchangeServerImpl.init {}` performs network/DB subscriptions in the constructor** — moving that to an explicit start call would change initialization timing/behavior; left as-is.
5. **⚠️ Duplicated `onBackPressed` (finish + slide animation)** in 7 activities — extracting to `BaseActivity` would silently change behavior for activities that don't currently override it (e.g. `MainActivity`, `AddActivity`, `SplashActivity`); not behavior-safe, skipped.
6. **⚠️ Presenters' nested Rx subscriptions without error handlers** (`ChoosePresenter.loadMiners`, `CurrencyPresenter.setCurrency`, `AddActivity`) — adding error handling changes failure-path behavior; left as-is.
7. **⚠️ `BaseAdapter` unsafe `(mDataList as ArrayList)` casts and mutable-list-as-`List` design** — redesign of the base data structure; risk outweighs benefit for a "minimal fixes" audit.
8. **⚠️ `CurrencyRepositoryImpl.fetchExchangeRates` performs DB writes inside the subscribe + raw `thread {}` per item** — threading rework would change behavior; left as-is.
9. **⚠️ `SettingsActivity`/`ThemesActivity` extend `BaseActivity`-less `MvpAppCompatActivity` (Choose/Themes) with hand-rolled status-bar handling** — full unification changes window-flag behavior; partial DRY (status-bar color) was fixed via `Theme.statusBarColorRes` instead.

---

## Verification

- `./gradlew test` — **BUILD SUCCESSFUL** (includes `MathUtilsTest`).
- `./gradlew assembleDebug` — **BUILD SUCCESSFUL**, APK produced.
- No calculation, network, routing or UI code path changed; all edits are structural (extractions, moves, deletions of unused/commented code) — behavior preserved.
