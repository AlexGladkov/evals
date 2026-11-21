# Privacy Policy — Mining Monitor for NiceHash

**Last updated:** [DATE]

**Developer:** [DEVELOPER NAME / COMPANY]  
**Contact:** [EMAIL]

## 1. Overview

Mining Monitor for NiceHash ("the App") is an unofficial monitoring tool for NiceHash cryptocurrency mining. This Privacy Policy explains how we handle information when you use the App.

**Key principle:** We do not collect, store, or share your personal data. The App is designed to function entirely on your device with minimal external communication — only to fetch public mining data from the NiceHash API.

## 2. Information We Do NOT Collect

We do **NOT** collect, store, transmit, or share:

- Personal identification information (name, email, phone number, etc.)
- Location data (GPS, coarse location)
- Device identifiers (AAID, IMEI, MAC address)
- Contacts, call logs, SMS
- Photos, media, files
- Browsing history
- Health, fitness, or biometric data
- Financial account credentials or API keys

## 3. Information Stored Locally on Your Device

The following information is stored **only on your device** using Android local storage (Room database) and is **never transmitted to us or third parties**:

| Data | Purpose | Storage |
|------|---------|---------|
| NiceHash BTC mining address(es) | To fetch your mining statistics from NiceHash public API | Local Room DB only |
| Miner name(s) / labels | User-defined labels for your miners | Local Room DB only |
| App preferences (theme, currency, language, tab) | User experience customization | Local SharedPreferences / Room DB |
| Cached exchange rates | Currency conversion display | Local Room DB only |

**You can delete all locally stored data at any time** by:
- Removing miners from the App settings, or
- Clearing the App data via Android Settings → Apps → Mining Monitor → Clear Data, or
- Uninstalling the App

## 4. Data Sent to External Services

### NiceHash API

The App sends your **BTC mining address** to the **NiceHash public API** (`api.nicehash.com`) to fetch mining statistics. This is a public, read-only API endpoint. The data retrieved includes:

- Mining profitability and hashrate
- Unpaid balance
- Payout history
- Worker status
- Global mining statistics

**We do not control how NiceHash processes this data.** Please refer to [NiceHash Privacy Policy](https://www.nicehash.com/support/privacy-policy) for their data practices.

### Google Play Billing

If you make an in-app purchase (Silver or Gold), the transaction is processed by **Google Play Billing**. We do not receive or store your payment information (credit card, etc.). Google processes payments according to [Google's Privacy Policy](https://policies.google.com/privacy).

## 5. No Analytics, No Tracking, No Ads

The App does **NOT** include:

- Analytics SDKs (Google Analytics, Firebase Analytics, etc.)
- Crash reporting SDKs (Crashlytics, Sentry, etc.)
- Advertising SDKs (AdMob, etc.)
- Third-party tracking or profiling
- Cookies or web tracking

The Firebase dependency in the codebase is **commented out and not active**.

## 6. Permissions Used

| Permission | Purpose |
|-----------|---------|
| `INTERNET` | Required to fetch mining data from NiceHash API |
| `POST_NOTIFICATIONS` | Required for future notification features (Android 13+) |
| `com.android.vending.BILLING` | Required for in-app purchases via Google Play |

The App does **NOT** request location, camera, microphone, contacts, or any other sensitive permissions.

## 7. Children's Privacy

The App is **not intended for children under 16**. We do not knowingly collect data from children. If you believe we have inadvertently collected such data, please contact us and we will delete it.

## 8. Data Security

We implement the following security measures:

- All data storage is local to your device (no cloud sync)
- Communication with NiceHash API uses HTTPS
- No external server infrastructure maintained by us

**However**, no method of electronic storage or transmission is 100% secure. We cannot guarantee absolute security of locally stored data if your device is compromised.

## 9. Your Rights

Depending on your jurisdiction (including GDPR in the EEA, CCPA in California), you may have rights to:

- Access your data
- Delete your data
- Data portability
- Object to processing

Since we do **not** collect or store any personal data on our servers, exercising these rights is straightforward:

- **Access:** All your data is on your device; you can view it in the App.
- **Delete:** Clear App data or uninstall.
- **Portability:** Not applicable — no data on our servers.
- **Object:** Not applicable — no processing by us.

For any privacy questions, contact us at [EMAIL].

## 10. Changes to This Policy

We may update this Privacy Policy from time to time. We will notify users of changes by updating the "Last updated" date. Significant changes may be communicated in the App or via Play Store listing.

## 11. Contact

For privacy-related inquiries:
- Email: [EMAIL]
- Developer: [DEVELOPER NAME]
