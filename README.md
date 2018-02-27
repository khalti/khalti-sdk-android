# khalti-android
SDK for Khalti Android App

## Installation
Add the following line to `dependency` section in `build.gradle` file

```
compile 'com.khalti:khalti-android:1.2.1'
```
It is recommended that you update your support libraries to the latest version. However, if you're unable to update the libraries add the following line instead.

```
compile ('com.khalti:khalti-android:1.2.1') {
        transitive = true
    }
```
Note : We recommend you to use the latest version of `Build tools` and `Support libraries` for maximum compatibility. 

In order to build and run this project, please use `Android Studio 3` and please note that the minimum `Build tools` and `Support libraries` version should be `27`.

```
compileSdkVersion 27
buildToolsVersion '27.0.3'

compile 'com.android.support:appcompat-v7:27.0.2'
```
In order to add support library 27, add the Google's maven url in your project level `build.gradle`

```
repositories {
        jcenter()
        mavenCentral()
        maven { url "https://maven.google.com" }
    }
```

`Important` Add the lines below in android default config of 'build.gradle'
``` java
renderscriptTargetApi 20
renderscriptSupportModeEnabled true
```

## Usage

### Layout

You can add KhaltiButton to your xml layout
```xml
<khalti.widget.KhaltiButton
            android:id="@+id/khalti_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>

```
And, Locate your xml Khalti Button in your Java
``` java
KhaltiButton khaltiButton = (KhaltiButton) findViewById(R.id.khalti_button);
```

Or, use it in Java

``` java
KhaltiButton khaltiButton = new KhaltiButton();
```
And, add this java KhaltiButton into your layout container.


### Configure

Configure Khalti Checkout by passing an instance of Config class

When instantiating Config class pass public key, product id, product name, product web url, amount (in paisa) and a new instance of OnCheckOutListener.
```java
Config config = new Config("Public Key", "Product ID", "Product Name", "Product Url", amount, new OnCheckOutListener() {

            @Override
            public void onSuccess(HashMap<String, Object> data) {
                Log.i("Payment confirmed", data+"");
            }

            @Override
            public void onError(String action, String message) {
                Log.i(action, message);
            }
        });
```
Additionally, Config class also accepts a HashMap parameter which you can use to pass any additional data. Make sure you add a `merchant_` prefix in your map key.

``` java
HashMap<String, String> map = new HashMap<>();
        map.put("merchant_extra", "This is extra data");
        
        Config config = new Config("Public Key", "Product ID", "Product Name", "Product Url", amount, map, new OnCheckOutListener() {

            @Override
            public void onSuccess(HashMap<String, Object> data) {
                Log.i("Payment confirmed", data);
            }

            @Override
            public void onError(String action, String message) {
                Log.i(action, message);
            }
        });

```
#### Set Config
Finally set your config in your KhaltiButton.

``` java
khaltiButton.setCheckOutConfig(config);
```

Check out the [documentation](http://docs.khalti.com/checkout/android/) for further details.

### [Changelog](https://github.com/khalti/khalti-sdk-android/blob/master/CHANGELOG.md)
