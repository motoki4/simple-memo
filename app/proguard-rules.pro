# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to the flags specified
# in /tools/proguard/proguard-android.txt. You can edit the include
# path and order by changing the proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
#
# Add any project specific keep options here:

-keep class androidx.room.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
