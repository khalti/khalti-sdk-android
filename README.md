Documentation of Khalti checkout for android

## Installation

Add the following line to `dependency` section in `build.gradle` file

```
implementation 'com.khalti:khalti-android:2.00.00'
```

It is recommended that you update your support libraries to the latest version. However, if you're unable to update the libraries add the following line instead.

```
implementation ('com.khalti:khalti-android:2.00.00') {
        transitive = true
    }
```

Note : We recommend you to use the latest version of `Build tools` and `Support libraries` for maximum compatibility.

In order to build and run this project, please use `Android Studio 3` and please note that the minimum `Build tools` and `Support libraries` version should be `28`.

```
compileSdkVersion 28
buildToolsVersion '28.0.0'

implementation 'com.android.support:appcompat-v7:28.0.0'
```

In order to add support library 28, add the Google's maven url in `build.gradle`

```
repositories {
        jcenter()
        mavenCentral()
        maven { url "https://maven.google.com" }
    }
```

We've upgraded to `AndroidX`. So we recommend using `AndroidX` dependencies instead of the old support libraries.

`Important` Add the lines inside android of your app 'build.gradle'

```java
compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
   }
```

`Important` Add the lines inside your 'gradle.properties'

```java
android.enableR8 = false
```

## Usage

### Layout

You can add KhaltiButton to your xml layout

```xml
<com.khalti.widget.KhaltiButton
            android:id="@+id/khalti_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>

```

And, Locate your xml Khalti Button in your Java

```java
KhaltiButton khaltiButton = (KhaltiButton) findViewById(R.id.khalti_button);
```

Or, use it in Java

```java
KhaltiButton khaltiButton = new KhaltiButton();
```

And, add this java KhaltiButton into your layout container.


### Configure

Configure Khalti Checkout by passing an instance of Config class. Build an object of Config class through a builder class.

```java
Builder builder = new Config.Builder(Constant.pub, "Product ID", "Main", 1100L, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap);
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("Payment confirmed", data+"");
            }
        });

Config config = builder
                .paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.KHALTI);
                    add(PaymentPreference.EBANKING);
                    add(PaymentPreference.MOBILE_BANKING);
                    add(PaymentPreference.CONNECT_IPS);
                    add(PaymentPreference.SCT);
                }})
                .additionalData(map)
                .build();
```

When building the Config, pass public key, product id, product name, amount (in paisa) and an implementation of OnCheckOutListener to the builder constructor. Then call the `build()` function on the builder object which will return a config object.

Additionally, the builder object can also be used to setup any additional data. Add any data to a `Map` and add it to the builder object as follows.

```java
Map<String, Object> map = new HashMap<>();
        map.put("merchant_extra", "This is extra data");
        
        Config config = builder
                .additionalData(map)
                .build();
```

Make sure you add a `merchant_` prefix in your map key.

Note : In order to preset mobile number, please use ```config.setMobile()```.

### Available Config Preset Methods

| Constraint | Method        | Description          |
|:-----------|:--------------|:---------------------|
| Optional   | `setMobile()` | Preset mobile number |

#### Set Config

Finally set your config in your KhaltiButton.

```java
khaltiButton.setCheckOutConfig(config);
```

## Summary

#### XML Attribute

| Attribute             | Description                                  |
|:----------------------|:---------------------------------------------|
| `khalti:text`         | Text to display                              |
| `khalti:button_style` | Set the style of KhaltiButton from 2 options |


#### Public Methods

| Constraint | Method                                    | Description                                               |
|:-----------|:------------------------------------------|:----------------------------------------------------------|
| Required   | `setCheckOutConfig(Config config)`        | Set configuration required by Khalti checkout             |
| Optional   | `setText(String text)`                    | Set text to display in KhaltiButton                       |
| Optional   | `setCustomView(View view)`                | Replace KhaltiButton's default view with your custom view |
| Optional   | `setButtonStyle(ButtonStyle buttonStyle)` | Select between 2 options to set KhaltiButton's style      |
| Optional   | `showCheckOut()`                          | Use this method to show Khalti checkout UI                |
| Optional   | `destroyCheckOut()`                       | Use this method to close Khalti checkout UI               |

#### Callback Methods

| Method                                   | Description                                                                                                                                                                                                                                                                                                                                                                               |
|:-----------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `onSuccess(HashMap data)`                | This method is called when a transaction has been completed and confirmed by the user. A map containing an access token, required to verify the transaction and data passed through Config instance is returned. Once this method is called, use the access token to verify the transaction. Please follow the [verification](./../api/verification.md) process for further instructions. |
| `onError(String action, String message)` | This method is called when an error occurs during payment initiation and confirmation. Action and message value is passed where action defines, the current action being performed and message defines the error.                                                                                                                                                                         |


##### Response Sample

###### Success Message

| Key              | Value        | Type   |
|:-----------------|:-------------|:-------|
| mobile           | 98XXXXXXXX   | String |
| product_name     | Product Name | String |
| product_identity | Product Id   | String |
| product_url      | Product Url  | String |
| amount           | 100          | Long   |
| token            | token        | String |

The success message also contains all the `key` and `value` provide as extra data while initiating `Config`

###### Error Message

| Variable | Description                          | Type   |
|:---------|:-------------------------------------|:-------|
| action   | Action performed - initiate, confirm | String |
| message  | Detail Error Message                 | String |

#### More Implementations

##### Method 1: With Custom Click Listener

Initialize the KhaltiCheckout Object

```java
KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, config);
```

Use `khaltiCheckout.show()` to display khalti widget

```java
khaltiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                khaltiCheckOut.show();
            }
        });
```

##### Method 2: With Custom View

Get your custom view

```java
View view = LayoutInflater.from(this).inflate(R.layout.custom_khalti_button, container, false);
```

Set custom view to your khalti button

```java
khaltiButton.setCustomView(view);
khaltiButton.setCheckOutConfig(config);
```


Check out the source for [Khalti checkout on Github](https://github.com/khalti/khalti-sdk-android).

Check out the [Verification](http://docs.khalti.com/api/verification/) process.

### [Changelog](https://github.com/khalti/khalti-sdk-android/blob/master/CHANGELOG.md)

