# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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



# ====================================================================
# Reglas específicas para Diefit (Room & Entidades)
# ====================================================================

# Conservar las clases de entidades para que R8/ProGuard no cambie sus nombres
-keep class com.program.diefit.entities.** { *; }

# Mantener la estructura interna de las consultas y la base de datos de Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**