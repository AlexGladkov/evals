# Play Store Assets & Screenshots Checklist

## Required Assets

### App Icon
- [ ] `ic_launcher` — Adaptive icon (foreground + background) in `mipmap-anydpi-v26`
- [ ] Legacy icons in: mdpi (48×48), hdpi (72×72), xhdpi (96×96), xxhdpi (144×144), xxxhdpi (192×192)
- [ ] Format: PNG, 512×512 for Play Store upload

**⚠️ Current icon:** Check that it does NOT resemble NiceHash official logo to avoid impersonation risk.

### Feature Graphic (1024 × 500 px)
- [ ] Attractive promotional banner for Play Store listing
- [ ] Should NOT use NiceHash branding/logos
- [ ] Suggested text: "Mining Monitor" + tagline "Your rigs, your phone, your peace of mind"
- [ ] Dark background preferred (fits mining/tech aesthetic)

### Screenshots (min 2, recommended 4-8)

Recommended screenshots (EN locale minimum):

1. **Income Tab** — Profitability dashboard (Hour/Day/Week/Month) with BTC and fiat values
2. **Balance Tab** — Balance by algorithm with hashrate and unpaid amounts
3. **Workers Tab** — Worker list with online/offline status and hashrate
4. **Charts** — Hashrate chart with time period selector (1h/6h/12h/1d/3d/1w)
5. **Payouts** — Payout history list
6. **Themes** — Theme selection screen showing Dark theme variant
7. **Add Miner** — Onboarding screen with BTC address input

**Screenshot specifications:**
- Format: PNG or JPEG (no alpha)
- Resolution: Minimum 320px, maximum 3840px
- Aspect ratio: 16:9 or 9:16 (phone screenshots)
- NO device frames (Play Console adds them automatically)
- NO promotional text overlaid on screenshots (Play Console policy)

### Video (optional, recommended)
- [ ] 30 sec – 2 min demo video
- [ ] Show real monitoring flow: open → see income → check workers → view chart
- [ ] Host on YouTube

## Screenshots to Capture from App

### Screen 1: Income Dashboard
- Screen: `MainActivity` → Income tab
- Show: Hour/Day/Week/Month profitability, BTC/USD rate, chart preview
- Use demo data or real miner with high activity

### Screen 2: Balance Tab
- Screen: `MainActivity` → Balance tab
- Show: Algorithm list with profitability, hashrate speed, unpaid balance
- Navigation bar info: Unpaid BTC / USD

### Screen 3: Workers
- Screen: `MainActivity` → Workers tab
- Show: Worker hubs with location grouping, worker status, hashrate
- Navigation bar: Workers online / Rigs online

### Screen 4: Hashrate Chart
- Screen: `MainActivity` → chart area
- Show: Line chart with 1d or 1w period, data points visible
- Multiple time selector buttons

### Screen 5: Payouts
- Screen: `MainActivity` → Payouts tab
- Show: Payout list with dates, BTC amounts, fees

### Screen 6: Choose Miner
- Screen: `ChooseActivity`
- Show: List of saved miners

### Screen 7: Settings
- Screen: `SettingsActivity`
- Show: Settings menu (Notifications, Themes, Currencies, Service Status, etc.)

### Screen 8: Dark Theme (if available)
- Screen: Any dashboard screen with Dark theme applied
- Show: Contrast between light and dark themes

## Text Overlay Guidelines (if adding captions)

For localized screenshots with captions, use simple labels:
- EN: "Monitor profitability in real time"
- EN: "Track balance across algorithms"
- EN: "Keep an eye on all your workers"

**DO NOT use:**
- "Official NiceHash app" / "Best NiceHash client"
- "Guaranteed earnings" / "Earn Bitcoin"
- NiceHash logos, colors, or brand elements
- Misleading claims about profitability

## Localization

- [ ] EN (English) — primary
- [ ] RU (Russian) — screenshots
- [ ] ZH (Chinese) — screenshots (if supported)

## Accessibility

- [ ] Screenshots show app in normal state (not empty/error states)
- [ ] Sufficient contrast visible

## File Naming Convention

```
play-store/
├── icon-512.png
├── feature-graphic-1024x500.png
├── screenshots/
│   ├── en/
│   │   ├── 01-income.png
│   │   ├── 02-balance.png
│   │   ├── 03-workers.png
│   │   ├── 04-chart.png
│   │   ├── 05-payouts.png
│   │   ├── 06-choose-miner.png
│   │   ├── 07-settings.png
│   │   └── 08-dark-theme.png
│   └── ru/
│       └── ... (same screenshots with Russian locale)
└── video/
    └── demo-video.mp4
```
