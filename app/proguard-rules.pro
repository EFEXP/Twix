# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android\sdk/tools/proguard/proguard-android.txt
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

-dontwarn sun.misc.Unsafe

-keep class android.support.v7.widget.SearchView { *; }

-dontwarn com.yalantis.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   long producerNode;
   long consumerNode;
}

-dontwarn com.squareup.okhttp.**
-dontwarn io.github.kexanie.**
-keep class com.x5.** { *; }
-dontwarn com.x5.**
#twitter4j
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }


# for EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-dontwarn javax.**
-dontwarn android.databinding.**
-keep class xyz.donot.quetzal.view.activity.MainActivity { *; }
-keep class xyz.donot.quetzal.util.RoundCorner{*;}
-dontwarn xyz.donot.**



