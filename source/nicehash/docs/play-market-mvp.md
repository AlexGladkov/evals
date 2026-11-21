# Play Market MVP Scope — Mining Monitor for NiceHash (Unofficial)

Версия: 1.0 (MVP)  
Статус: Pre-launch  
Дата: 2025

## Позиционирование

**Mining Monitor for NiceHash** — неофициальный Android-мониторинг NiceHash-майнинга. Быстрая проверка доходности, баланса, выплат, воркеров и хешрейта без входа в полный аккаунт. Read-only публичный доступ по BTC-адресу.

> **Важно:** Это неофициальное приложение. Не аффилировано с NiceHash/H-BIT d.o.o. Не майнит на устройстве. Не является финансовым советником.

## Целевая аудитория

- **Primary:** Home/hobby майнеры (1-5 ригов, GPU/ASIC на NiceHash) — «быстро глянуть с телефона».
- **Secondary:** Small farm operators (5-50 ригов) — мониторинг uptime, алерты.
- **Tertiary:** Seasonal miners — включаются при росте рынка.

## MVP Core Features

### 1. Управление майнерами (Miners)
- Добавление майнера по NiceHash BTC-адресу
- Список сохранённых майнеров (ChooseActivity)
- Выбор активного майнера
- Редактирование имени
- Удаление

### 2. Дашборд (MainActivity + табы)
- **Income (Доходность):** Hour / Day / Week / Month в BTC + фиате. Курс BTC/USD.
- **Balance (Баланс):** Баланс по алгоритмам (algo, hashrate, profitability, unpaid). Income/Day по каждому алгоритму.
- **Payouts (Выплаты):** История выплат (дата, сумма BTC, комиссия).
- **Workers (Воркеры):** Группировка по локациям/хабам, статус, hashrate, diff, uptime.

### 3. Графики хешрейта
- Периоды: 1h, 6h, 12h, 1d, 3d, 1w
- Данные через `stats.provider.ex` API
- Pull-to-refresh на всех экранах

### 4. Мультивалютность
- Поддержка: BTC, USD, RUB, EUR, GBP, AUD, BGN, BRL, CAD, CHF, CNY, CZK, DKK (+ другие)
- Конвертация через exchange server

### 5. Сервис-статус
- Статус endpoints NiceHash API
- Статус серверов по регионам (Brazil, China, Japan, Europe, US)

### 6. Настройки
- Выбор темы (Light/Dark, несколько вариантов)
- Выбор валюты по умолчанию
- Выбор языка (English, Russian, Chinese)
- Выбор таба по умолчанию
- Настройки уведомлений (worker/algo down, payments, maintenance, звук/вибро)

### 7. Платные функции (In-App Purchases)
- **Silver:** No ads, Themes, 3 miners
- **Gold:** No ads, Themes, 6 months notifications
- Экран Purchases с информацией о подписках

### 8. Onboarding (MVP-путь)
1. Splash → загрузка конфигурации
2. Если нет майнеров → Add Miner (объяснение, поле BTC-адреса)
3. После добавления → Main Dashboard (Income по умолчанию)

## Технический фундамент (контекст)
- **Стек:** Kotlin, MVP/Moxy, Dagger 2, RxJava2, Retrofit, Room, XML/ViewBinding
- **minSdk:** 21, **targetSdk:** 33 (⚠️ требует повышения до 35+)
- **API:** NiceHash public API (stats.provider, stats.provider.ex, stats.provider.payments, stats.global.current/24h)
- **Хранение:** Локальное (Room) — конфигурация, майнеры, кэш валют
- **Никакого сбора данных на внешние серверы**
- **Firebase:** закомментирован, не используется
- **Реклама:** отсутствует (монетизация только IAP)

## Что НЕ входит в MVP
- Авторизация через NiceHash API ключи (только публичный BTC-адрес)
- Push-уведомления (требует Firebase/FCM + бэкенд)
- Виджеты Android
- Мульти-аккаунт с переключением без выбора
- Экспорт данных (CSV)
- Hardware-мониторинг (температура, fan speed) — NiceHash API не даёт
- Глубокий historical analytics (>1 недели)
- Wear OS
- Тёмная тема как paid (все темы доступны)

## Ключевые метрики успеха MVP
- Успешное добавление miner'а и отображение данных за <30 секунд
- Crash-free rate > 99%
- Возврат в приложение (Day-1, Day-7 retention)
- Конверсия в Silver/Gold (цель: >2%)
- Оценка в Google Play > 4.0

## Ближайшие шаги после MVP
1. Push-уведомления (worker down, payout received)
2. Графики profitability (не только hashrate)
3. Больше периодов графиков (1m, 3m)
4. Виджеты
5. Миграция на targetSdk 35+
6. Замена IAB v3 на Play Billing Library 7+
