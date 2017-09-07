# khalti-android
SDK for Khalti Android App

## Installation
Add the following line to `dependency` section in `build.gradle` file

```
compile 'com.khalti:khalti-checkout:0.1.2'
```
Note : We recommend you use the latest version of build tools and support libraries for maximum compatibility.

## Setup
Add these lines to the default config section in `build.gradle` file

```
renderscriptTargetApi 20'

renderscriptSupportModeEnabled true
```

## Usage
Add KhaltiButton to your layout
```xml
<khalti.widget.KhaltiButton
            android:id="@+id/khalti_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>
```
```java
Config config = new Config(pub, "123", "Gaida Churot", "http://churot.com/gaida", 1000L, new OnCheckOutListener() {

            @Override
            public void onSuccess(HashMap<String, Object> data) {
                LogUtil.i("Payment confirmed", data);
            }

            @Override
            public void onError(String action, String message) {
                LogUtil.i(action, message);
            }
        });
```

```java
HashMap<String, Object> map = new HashMap<>();
        map.put("extra", "this is gaida churot");
        
        Config config = new Config(pub, "123", "Gaida Churot", "http://churot.com/gaida", 1000L, new OnCheckOutListener() {

            @Override
            public void onSuccess(HashMap<String, Object> data) {
                LogUtil.i("Payment confirmed", data);
            }

            @Override
            public void onError(String action, String message) {
                LogUtil.i(action, message);
            }
        });
```

```java
KhaltiButton kButton = findViewById(R.id.khalti_button);
kButton.setConfig(config);
```
## Sample App
