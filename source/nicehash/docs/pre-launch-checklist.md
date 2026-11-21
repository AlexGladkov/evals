# Google Play Pre-Launch Checklist — Mining Monitor for NiceHash

Версия: 1.0 (MVP)  
Статус: ⚠️ NOT READY — критические блокеры перед публикацией

## 🔴 Critical Blockers (must fix before launch)

### B1. targetSdk Upgrade
- [ ] **Текущий:** targetSdk 33  
- [ ] **Требуется:** targetSdk 35 (Android 15) как минимум, для новых публикаций в 2025
- [ ] Проверить актуальное требование в [Play Console](https://play.google.com/console) → Policy → Target API level
- [ ] Протестировать на Android 14/15 устройстве/эмуляторе
- [ ] Учесть: foreground service restrictions, notification permissions (POST_NOTIFICATIONS), edge-to-edge enforcement

### B2. In-App Billing Library Upgrade
- [ ] **Текущий:** IAB v3 (`com.anjlab.android.iab.v3:library:1.0.44`) — УСТАРЕЛА
- [ ] **Требуется:** Google Play Billing Library 7.x+ (март 2025: 7.x минимально)
- [ ] Заменить в `app/build.gradle`
- [ ] Поддержать acknowledge purchases
- [ ] Поддержать pending transactions
- [ ] Протестировать покупки Silver/Gold через Play Billing

### B3. Package Name & Branding
- [ ] **Текущий package:** `com.dev.nicehash` — ВЫСОКИЙ РИСК trademark violation
- [ ] **Рекомендация:** Сменить package до публикации (после публикации сменить нельзя)
- [ ] Варианты: `com.dev.miningmonitor`, `com.hashmonitor.app`, `com.dev.nhmonitor`
- [ ] **Текущее название:** "Nicehash" — риск impersonation
- [ ] **Рекомендация:** "Mining Monitor for NiceHash" с явным "Unofficial"
- [ ] Убрать любые логотипы/цвета, напоминающие официальный NiceHash

### B4. Privacy Policy
- [ ] Создать/разместить privacy policy (см. `docs/privacy-policy-draft.md`)
- [ ] URL должен быть доступен публично (GitHub Pages, сайт, Google Docs published)
- [ ] Заполнить Data Safety form в Play Console (честно: BTC-адрес передаётся в NiceHash API)
- [ ] Проверить: никакого сбора данных на свои серверы

### B5. Crypto Disclaimer
- [ ] Добавить in-app disclaimer (см. `docs/crypto-disclaimer.md`)
- [ ] Добавить disclaimer в store listing
- [ ] В Play Console: задекларировать «нет финансовых услуг» (только monitoring)
- [ ] Проверить: нет обещаний заработка в текстах

---

## 🟡 High Priority (should fix before launch)

### H1. Permissions Audit
- [ ] `INTERNET` — объяснить в listing/privacy: «for fetching mining data from NiceHash API»
- [ ] `POST_NOTIFICATIONS` — объяснить: «for future push notifications»
- [ ] `BILLING` — стандарт для IAP
- [ ] Убедиться, что нет неиспользуемых permissions в манифесте

### H2. Firebase Cleanup
- [ ] `google-services.json` присутствует, но Firebase закомментирован в коде
- [ ] Удалить `google-services.json` если Firebase не используется
- [ ] Удалить/закомментировать `apply plugin: 'com.google.gms.google-services'` в `app/build.gradle`

### H3. ProGuard / R8
- [ ] `minifyEnabled false` в release — включить для production
- [ ] Проверить ProGuard rules для: Retrofit models, Room entities, Moxy, Dagger
- [ ] Протестировать release build с R8

### H4. Crash Testing
- [ ] Запустить `./gradlew testDebugUnitTest`
- [ ] Пройти ручной smoke test: Splash → Add Miner → Main Dashboard (все табы) → Settings → Back
- [ ] Проверить empty states: нет майнера, нет выплат, нет воркеров, нет графика
- [ ] Проверить error states: нет интернета, API недоступен
- [ ] Проверить ротацию экрана на всех экранах

### H5. Content Rating
- [ ] Заполнить content rating questionnaire в Play Console
- [ ] Ожидаемый рейтинг: 16+ (финансовый контент, криптовалюта)
- [ ] Проверить: нет ограничений по странам для crypto-приложений

---

## 🟢 Medium Priority (nice to have before launch)

### M1. App Versioning
- [ ] `versionCode 4` — для первого production-релиза обычно начинают с 1
- [ ] `versionName "1.0"` — ок

### M2. Store Listing Localization
- [ ] EN listing (основной)
- [ ] RU listing
- [ ] ZH listing (если поддерживается китайский)

### M3. Screenshots
- [ ] Подготовить 4-8 скриншотов (см. `docs/assets-and-screenshots.md`)
- [ ] Feature graphic 1024×500
- [ ] Иконка 512×512

### M4. Test Track
- [ ] Загрузить в Internal Testing Track
- [ ] Пригласить 5-10 тестеров
- [ ] Собрать feedback → исправить → promote to Production

### M5. Support Contact
- [ ] Email для поддержки (указать в store listing)
- [ ] Website (хотя бы ссылка на privacy policy)

### M6. API Terms Verification
- [ ] Проверить [NiceHash API Terms](https://www.nicehash.com/docs/) на предмет разрешения third-party apps
- [ ] Добавить attribution: "Data provided by NiceHash API"

---

## ✅ Done / Not Applicable

- [x] No user authentication — только публичный BTC-адрес
- [x] No cloud backend — всё локальное
- [x] No analytics SDKs
- [x] No advertising SDKs
- [x] No social media integrations
- [x] No location permissions
- [x] minSdk 21 (поддерживает 99%+ устройств)
- [x] compileSdk 35 (актуальный)

---

## Launch Sequence (Recommended)

```
1. Rename package + app name (B3)
2. Upgrade targetSdk → 35 (B1)
3. Replace IAB v3 → Play Billing Library (B2)
4. Publish Privacy Policy URL (B4)
5. Add in-app disclaimers (B5)
6. Test with R8 enabled (H3)
7. Prepare store listing (EN) + screenshots (M3)
8. Upload to Internal Testing (M4)
9. QA pass on real devices
10. Submit for Production review
```

---

## Post-Launch

- [ ] Monitor Play Console for policy violations / warnings
- [ ] Respond to user reviews in first 48h
- [ ] Track crash rate via Play Console → Android Vitals
- [ ] Plan v1.1: push notifications, more chart periods, targetSdk maintenance
