# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/ishwor/Workspace/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#Support
#-dontwarn android.support.v7.**
#-keep class android.support.v7.** { *; }
#-keep interface android.support.v7.** { *; }

#Design library
#-dontwarn android.support.design.**
#-keep class android.support.design.** { *; }
#-keep interface android.support.design.** { *; }
#-keep public class android.support.design.R$* { *; }

#Carbon Material Design
#-keep class khalti.carbonX.widget.** { *; }
#-dontwarn khalti.carbonX.widget.**
#-keepclasseswithmembernames class * {
 #  native <methods>;
#}
#Animations
-keep class com.nineoldandroids.animation.** { *; }
-keep interface com.nineoldandroids.animation.** { *; }
-keep class com.nineoldandroids.view.** { *; }
-keep interface com.nineoldandroids.view.** { *; }

# Gson
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
 -keep class com.google.code.gson**{ *; }

 # Retrofit 2.X
 -dontwarn retrofit2.**
 -keep class retrofit2.** { *; }
 -keepattributes Signature
 -keepattributes Exceptions

 -keepclasseswithmembers class * {
     @retrofit2.http.* <methods>;
 }

 -keep class com.squareup.okhttp.** { *; }
 -keep interface com.squareup.okhttp.** { *; }

 -dontwarn com.squareup.okhttp.**
 -dontwarn okio.**
 -dontwarn retrofit.**

 -dontwarn okio.**
 -dontwarn javax.annotation.Nullable
 -dontwarn javax.annotation.ParametersAreNonnullByDefault

 # If in your rest service interface you use methods with Callback argument.
 -keepattributes Exceptions


 # If your rest service methods throw custom exceptions, because you've defined an ErrorHandler.
 -keepattributes Signature

 #Apache
 -dontwarn org.apache.**

 #utils
 -keep class com.khalti.utils.** { *; }

 #Config
-keep class com.khalti.checkout.helper.Config {*;}
-keep class com.khalti.checkout.helper.Config$* {*;}
-keepclassmembers class com.khalti.checkout.helper.Config** {*;}

 -ignorewarnings
