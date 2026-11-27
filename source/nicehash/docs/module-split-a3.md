# A3 Report: Monolith → Gradle Modules

## Module structure

```
        ┌─────────────────────────────────────────────┐
        │ :app (com.android.application)              │
        │  app/ activities, fragments, presenters,    │
        │      views, adapters, compose, servers,     │
        │      navigation, di (all Dagger @Module +   │
        │      @Component stay here)                  │
        │  base/ (Android/BaseAdapter base classes)   │
        │  helpers/ (ImageLoader, ListConfig,         │
        │     PaintUtils, SizeContract, WindowUtils,  │
        │     CurrencyResources)                      │
        │  enums/ (Keys, ScreenKeys — navigation)     │
        └──────────────┬──────────────────────────────┘
                       │ implementation
                       ▼
        ┌─────────────────────────────────────────────┐
        │ :data (com.android.library)                 │
        │  data/implementations/ (repository impls)   │
        │  data/providers/ (API providers, services,  │
        │     api models)                             │
        │  data/converters/                           │
        │  data/room/ (entities, DAOs, database)      │
        │  contracts/ (RoomContract, RemoteContract)  │
        └──────────────┬──────────────────────────────┘
                       │ implementation
                       ▼
        ┌─────────────────────────────────────────────┐
        │ :domain (java-library + kotlin, PURE JVM)   │
        │  domain/models/  (Income, Balance, Miner…)  │
        │  domain/repositories/ (interfaces)          │
        │  helpers/ (EnumCollections, MathUtils)      │
        │  enums/Theme                                │
        │  deps: kotlin-stdlib, rxjava2 only —        │
        │  no AGP, no androidx, no android.*          │
        └─────────────────────────────────────────────┘

Dependency graph (acyclic):  :app ──▶ :data ──▶ :domain
Benchmarks (:macrobenchmark, :microbenchmark) untouched.
```

## Why these boundaries

- **:domain** — business models + repository interfaces. Kept Android-free by:
  - dropping `Parcelable` from `Balance` (its `CREATOR` was never referenced anywhere) and from
    `Miner` (now `Serializable`; the single `putParcelable` call site in `ChoosePresenter.editClick`
    now uses `putSerializable`, and its navigation target is disabled/commented-out code, so runtime
    behavior is unchanged);
  - moving `enums/Theme`, `helpers/EnumCollections`, `helpers/MathUtils` next to their consumers
    (all three are imported by both :domain and :data, so they must live below :data);
  - `EnumCollections.Currency/Language` carried `R.string`/`R.drawable` ids; those resource fields
    moved to the app-side mapping `helpers/CurrencyResources` with identical values, so the pure
    module has no `R`. All other `EnumCollections` call sites only use `.id`/`.intCode`.
  - Helper placement rule: helpers consumed by non-UI layers went to :domain; Android-only helpers
    stayed in :app. `SizeContract` is used only by `MainActivity` → stays in :app.
  - `MinerRepository` had an unused `import ...data.providers.models.ApiPayment` — removed (it was
    the only domain→data edge; the interface only exposes domain types).
- **:data** — everything that touches network/storage: repository impls, providers, converters,
  Room. `contracts/` moved here "by usage": `RoomContract` is referenced only by Room code,
  `RemoteContract` by Retrofit services (:data) and `RemoteModule` (:app, allowed: app→data).
- **:app** — UI, Moxy presenters, navigation, Dagger graph, Android base classes, benchmarks
  integration. Package names were deliberately preserved across the move so call sites keep
  compiling unchanged.

## DI / Room / kapt changes

- **Dagger**: the simplest option, as allowed by the task — `AppComponent` and all `@Module`
  classes (`RoomModule`, `RemoteModule`, `RepositoryModule`, `ConverterModule`, `ProviderModule`,
  …) stay in :app and simply reference :data classes, which are on the compile classpath via
  `implementation project(':data')`. No dagger annotations were added to :data, so :data needs no
  dagger-compiler.
- **Room**: entities, DAOs and `RoomAppDataSource` moved to :data; `kapt(room-compiler)` now runs
  in :data (per-module kapt). The generated `*_Impl` classes live in :data, and `RoomModule`
  (still in :app) builds the database through `RoomAppDataSource.buildDataSource` — same call as
  before, same DB name/version/migration policy → schema and behavior unchanged.
- **kapt**: :app keeps `kapt(dagger-compiler)` + `kapt(moxy-compiler)`; :data gets
  `kapt(room-compiler)`. `correctErrorTypes = true` kept in both.

## API-surface deltas (compile-only, no behavior change)

1. `EnumCollections.Currency` lost `titleRes`/`flagRes`; `Language` lost `title` (only ever used
   from commented-out code). Replacement: `CurrencyResources.titleRes/flagRes` in :app with the
   same ids; `CurrencyPresenter` is the only consumer and was updated.
2. `Miner` is `Serializable` instead of `Parcelable` (see above); `Balance`/`Configuration` no
   longer (pseudo-)implement Parcelable — `Configuration`'s Parcelable import was dead.
3. Test `MathUtilsTest` moved with `MathUtils` to `:domain/src/test` and passes there
   (`:domain:test` green).

## Verification

- `./gradlew :domain:test` — green (includes moved `MathUtilsTest`)
- `./gradlew test` — green (all modules, all variants)
- `./gradlew assembleDebug` — green (:app APK + :data AAR + benchmarks untouched)
