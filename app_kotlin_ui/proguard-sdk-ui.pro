# Sdk-ui rule

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.* {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.* {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class ru.rbs.mobile.payment.sample.kotlin.**$$serializer { *; }
-keepclassmembers class ru.rbs.mobile.payment.sample.kotlin.* {
    *** Companion;
}
-keepclasseswithmembers class ru.rbs.mobile.payment.sample.kotlin.threeds.* {
    kotlinx.serialization.KSerializer serializer(...);
}