# khalti-android
SDK for Khalti Android App

## Installation
Add the following line to `dependency` section in `build.gradle` file

```
compile 'com.khalti:khalti-checkout:0.1.2'
```
Note : We recommend you use the latest version of `Build tools` and `Support libraries` for maximum compatibility. 

In order to build and run this project, please use `Android Studio 3` and the minimum `Build tools` and `Support libraries` version should be `26`.

```
compileSdkVersion 26
buildToolsVersion '26.0.1'

compile 'com.android.support:appcompat-v7:26.0.1'
```

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
Config config = new Config("Public Key", "Product ID", "Product Name", "Product Url", amount, new OnCheckOutListener() {

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
        map.put("extra", "This is extra data");
        
        Config config = new Config("Public Key", "Product ID", "Product Name", "Product Url", amount, new OnCheckOutListener() {

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
