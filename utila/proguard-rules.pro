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

#Utils
 -keep class com.utila.** { *; }

 #Support
 #-dontwarn android.support.v7.**
 #-keep class android.support.v7.** { *; }
 #-keep interface android.support.v7.** { *; }

 #Design library
 #-dontwarn android.support.design.**
 #-keep class android.support.design.** { *; }
 #-keep interface android.support.design.** { *; }
 #-keep public class android.support.design.R$* { *; }

 # Rxjava
  -keep class rx.schedulers.Schedulers {
      public static <methods>;
  }
  -keep class rx.schedulers.ImmediateScheduler {
      public <methods>;
  }
  -keep class rx.schedulers.TestScheduler {
      public <methods>;
  }
  -keep class rx.schedulers.Schedulers {
      public static ** test();
  }
  -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
      long producerIndex;
      long consumerIndex;
  }
  -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
      long producerNode;
      long consumerNode;
  }

  #Retrolambda
  -dontwarn java.lang.invoke.*