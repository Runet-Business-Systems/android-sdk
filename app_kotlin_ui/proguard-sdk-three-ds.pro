# ThreeDS rules
-android
-dontpreverify
-keepattributes *Annotation*
-dontskipnonpubliclibraryclasses
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable,Exceptions
-repackageclasses 'com.bpcbt.threeds2.android'

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.bpcbt.threeds2.android.impl.pojo.** { *; }

-keepclassmembers class **.R$* {
    public static <fields>;
}

# package com.bpcbt.threeds2.android.spec
-keep public class com.bpcbt.threeds2.android.spec.*
-keepclassmembers public class com.bpcbt.threeds2.android.spec.* {
    public <methods>;
}
-keep public interface com.bpcbt.threeds2.android.spec.*
-keepclassmembers public interface com.bpcbt.threeds2.android.spec.* {
    public <methods>;
}
-keepclassmembers public class com.bpcbt.threeds2.android.spec.* extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.bpcbt.threeds2.android.impl.Factory
-keepclassmembers class com.bpcbt.threeds2.android.impl.Factory {
    public <methods>;
}

-assumenosideeffects class com.bpcbt.threeds2.android.impl.L {
    public static void SHOW_MESSAGE(...);
}

# package org.spongycastle
-keep public class org.spongycastle.** {
  public protected *;
}
-keep public interface org.spongycastle.** {
  public protected *;
}
-dontwarn org.spongycastle.**

# package com.nimbusds
-keep public class com.nimbusds.** {
  public protected *;
}
-keep public interface com.nimbusds.** {
  public protected *;
}
-dontwarn com.nimbusds.**

# package com.ults.listeners
-keep public class com.ults.listeners.** {
  public protected *;
}
-keep public interface com.ults.listeners.** {
  public protected *;
}
-dontwarn com.ults.listeners.**