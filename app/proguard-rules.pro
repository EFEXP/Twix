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
-dontwarn javax.**
-dontwarn android.databinding.**
-keep class xyz.donot.quetzal.util.RoundCorner{*;}
-dontwarn xyz.donot.**


##---------------Begin: proguard configuration common for all Android apps ----------
-optimizationpasses 2
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''

