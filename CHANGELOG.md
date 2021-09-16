# Changelog

### 2.01.00
- 

### 2.00.01
- Fixed `onCheckOutLister.onSucess()` not being called if additional data is not passed through config

### 2.00.00

- Migrated to AndroidX
- Updated Api Urls to V2
- Migrated to Kotlin
- Migrated to Coroutine from RxJava
- Removed RxBus in favor of a simple listener
- Added preference option in config to control payment tabs locally
- Cleaned up animation libraries
- Updated Khalti logo and color
- Implemented new design
- Updated Khalti payment flow; Khalti PIN is now required to initiate payment
- Updated config to give more granular control
- Added SCT and Connect IPS payment option
- Khalti PIN field input length has been increased to 10
- Khalti PIN attempts is shown if the user enters wrong PIN
- A test banner is shown if test token is used

### 1.02.09

- Updated picasso version
- Fixed crashing due to picasso
- Updated version naming to x.xx.xx format
- Package has been moved from khalti to com.khalti

### 1.2.5

- Removed sms permission

### 1.2.3

- Fixed error message in amount in KhaltiCheckOut
- Made product url optional in Config

### 1.2.2

- Added mobile preset to both e-banking and card payment
- Fixed bank logo size issue in bank contact form

### 1.2.1

- Fixed static method not found issue (Downgraded retrolambda to 3.2.3)
- Bug fixes and minor tweaks

### 1.2.0

- Added Card (Debit/Credit) as a payment method
- Changed bank list to searchable grid view list with bank logo
- Merchant can now order and visibility of payment option tabs
- Mobile number can be preset in wallet form
- Merchant extra info map changed from <String, Object> to <String, String>
- Bug fixes and minor tweaks
