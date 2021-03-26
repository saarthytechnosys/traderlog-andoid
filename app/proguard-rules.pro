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

# Default PV proguard file - use it and abuse it if its useful.

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclassmembers
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some of these optimisations on its own).
# -dontoptimize #Only uncomment this if you are addressing Android 2.X or lower)
-dontpreverify

# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.


##########
# Maintain all attributes:
# To avoid having to add each in several different places
# below.
#
# You may need to keep Exceptions if using dynamic proxies
# (e. g. Retrofit), Signature and *Annotation* if using reflection
# (e. g. Gson's ReflectiveTypeAdapterFactory).
##########
-keepattributes Exceptions,InnerClasses,Signature,Deprecated, SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

##########
# Android:
##########
##########
# Those are no longer required as this will force ProGuard to keep
# not only real app components and views, but also stuff like
# BaseFragmentActivityApi16, BaseFragmentActivityApi14,
# SupportActivity etc from being merged or removed.
# AAPT generates rules for all classes which were mentioned in XMLs.
##########
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
# Data Binding
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }
#This is used if you are using onClick on the XML.. you shouldn't :-)
-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

##########
# View - Gets and setters - keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
##########

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

##########
#Enums - For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
##########

-keepclassmembers enum * { *; }

##########
# Parcelables: Mantain the parcelables working
##########
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

#############
# Serializables
#############
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
##########
# Kotlin
##########
-dontwarn kotlin.**
-dontnote kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}

#Ignore null checks at runtime
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}
#############
# BottomBar (Needed to call methods via reflection to customize it)
#############
-keep class android.support.design.internal.** { *; }


#############
# WebViews
#############
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class android.support.v8.renderscript.** { *; }

########################################
# External Libraries
########################################


#############
# Google Play Services
#############
-keep class com.google.android.gms.* {  *; }
-dontwarn com.google.android.gms.**
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-dontnote **ILicensingService
-dontnote com.google.android.gms.gcm.GcmListenerService
-dontnote com.google.android.gms.**

-dontwarn com.google.android.gms.ads.**
#############
# Android Support Lib
#############
-keep class android.support.design.widget.TextInputLayout { *; }
#############
# Firebase
#############
-dontnote com.google.firebase.**
-dontwarn com.google.firebase.crash.**


##########
# Android architecture components: Lifecycle ( https://issuetracker.google.com/issues/62113696 )
##########
# LifecycleObserver's empty constructor is considered to be unused by proguard
-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}
# ViewModel's empty constructor is considered to be unused by proguard
-keepclassmembers class * extends android.arch.lifecycle.ViewModel {
    <init>(...);
}
# keep Lifecycle State and Event enums values
-keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
-keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
# keep methods annotated with @OnLifecycleEvent even if they seem to be unused
# (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
-keepclassmembers class * {
    @android.arch.lifecycle.OnLifecycleEvent *;
}
#############
# Retrofit
#############
-dontnote okio.**

#############
# HttpClient Legacy (Ignore) - org.apache.http legacy
#############
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
##########
# Glide
##########
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-dontnote com.bumptech.glide.**
##########
# RxJava 2
##########

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
#############
# Stetho
#############
-dontnote com.facebook.stetho.**
##########
# Crashlytics:
# Adding this in to preserve line numbers so that the stack traces can be remapped
##########
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable


##########
# Airbnb: Deep Linking Dispatch
##########

-keep class com.airbnb.deeplinkdispatch.** { *; }
-keepclasseswithmembers class * {
     @com.airbnb.deeplinkdispatch.DeepLink <methods>;
}

-keep @interface your.package.deeplinks.** { *; }
-keepclasseswithmembers class * {
    @your.package.deeplinks.* <methods>;
}

-dontnote com.airbnb.deeplinkdispatch.**
#############
# Carousel View
#############
-dontnote com.synnapps.carouselview.**
##########
# BlurView
##########
-dontnote eightbitlab.com.blurview.**


##########
# cwac-netsecurity
##########
-keep class com.commonsware.cwac.netsecurity.** { *; }

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
#-keep class com.crashlytics.** { *; }
#-dontwarn com.crashlytics.**

-dontwarn android.databinding.**
-keep class android.databinding.** { *; }

 -dontwarn okio.**
    -keepattributes InnerClasses
    -dontwarn sun.misc.**
    -dontwarn java.lang.invoke.**
    -dontwarn okhttp3.**
    -dontwarn com.anchorfree.sdk.**
    -dontwarn okio.**
    -dontwarn javax.annotation.**
    -dontwarn org.conscrypt.**
    -keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
    #DNSJava
    -keep class org.xbill.DNS.** {*;}
    -dontnote org.xbill.DNS.spi.DNSJavaNameServiceDescriptor
    -dontwarn org.xbill.DNS.spi.DNSJavaNameServiceDescriptor
    -keep class * implements com.google.gson.TypeAdapterFactory
    -keep class * implements com.google.gson.JsonSerializer
    -keep class * implements com.google.gson.JsonDeserializer
    -keep class com.anchorfree.sdk.SessionConfig { *; }
    -keep class com.anchorfree.sdk.fireshield.** { *; }
    -keep class com.anchorfree.sdk.dns.** { *; }
    -keep class com.anchorfree.sdk.HydraSDKConfig { *; }
    -keep class com.anchorfree.partner.api.ClientInfo { *; }
    -keep class com.anchorfree.sdk.NotificationConfig { *; }
    -keep class com.anchorfree.sdk.NotificationConfig$Builder { *; }
    -keep class com.anchorfree.sdk.NotificationConfig$StateNotification { *; }
    -keepclassmembers public class com.anchorfree.ucr.transport.DefaultTrackerTransport {
       public <init>(...);
     }
     -keepclassmembers class com.anchorfree.ucr.SharedPrefsStorageProvider{
        public <init>(...);
     }
     -keepclassmembers class com.anchorfree.sdk.InternalReporting$InternalTrackingTransport{
     public <init>(...);
     }
     -keep class com.anchorfree.sdk.exceptions.* {
        *;
     }

    -keepclassmembers class * implements javax.net.ssl.SSLSocketFactory {
        final javax.net.ssl.SSLSocketFactory delegate;
    }

    # https://stackoverflow.com/questions/56142150/fatal-exception-java-lang-nullpointerexception-in-release-build
    -keepclassmembers,allowobfuscation class * {
      @com.google.gson.annotations.SerializedName <fields>;
    }

    # Default PV proguard file - use it and abuse it if its useful.

    # If you keep the line number information, uncomment this to
    # hide the original source file name.
    #-renamesourcefileattribute SourceFile

    -dontusemixedcaseclassnames
    -dontskipnonpubliclibraryclassmembers
    -verbose

    # Optimization is turned off by default. Dex does not like code run
    # through the ProGuard optimize and preverify steps (and performs some of these optimisations on its own).
    # -dontoptimize #Only uncomment this if you are addressing Android 2.X or lower)
    -dontpreverify

    # Note that if you want to enable optimization, you cannot just
    # include optimization flags in your own project configuration file;
    # instead you will need to point to the
    # "proguard-android-optimize.txt" file instead of this one from your
    # project.properties file.


    ##########
    # Maintain all attributes:
    # To avoid having to add each in several different places
    # below.
    #
    # You may need to keep Exceptions if using dynamic proxies
    # (e. g. Retrofit), Signature and *Annotation* if using reflection
    # (e. g. Gson's ReflectiveTypeAdapterFactory).
    ##########
    -keepattributes Exceptions,InnerClasses,Signature,Deprecated, SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

    ##########
    # Android:
    ##########
    ##########
    # Those are no longer required as this will force ProGuard to keep
    # not only real app components and views, but also stuff like
    # BaseFragmentActivityApi16, BaseFragmentActivityApi14,
    # SupportActivity etc from being merged or removed.
    # AAPT generates rules for all classes which were mentioned in XMLs.
    ##########
    #-keep public class * extends android.app.Activity
    #-keep public class * extends android.app.Application
    #-keep public class * extends android.app.Service
    #-keep public class * extends android.content.BroadcastReceiver
    #-keep public class * extends android.content.ContentProvider
    #-keep public class * extends android.app.backup.BackupAgentHelper
    #-keep public class * extends android.preference.Preference
    # Data Binding
    -dontwarn android.databinding.**
    -keep class android.databinding.** { *; }
    #This is used if you are using onClick on the XML.. you shouldn't :-)
    -keepclassmembers class * extends android.content.Context {
       public void *(android.view.View);
       public void *(android.view.MenuItem);
    }

    ##########
    # View - Gets and setters - keep setters in Views so that animations can still work.
    # see http://proguard.sourceforge.net/manual/examples.html#beans
    ##########

    -keepclassmembers public class * extends android.view.View {
       void set*(***);
       *** get*();
    }

    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet, int);
    }

    ##########
    #Enums - For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
    ##########

    -keepclassmembers enum * { *; }

    ##########
    # Parcelables: Mantain the parcelables working
    ##########
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    -keepclassmembers class * implements android.os.Parcelable {
        static ** CREATOR;
    }

    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    #############
    # Serializables
    #############
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }
    ##########
    # Kotlin
    ##########
    -dontwarn kotlin.**
    -dontnote kotlin.**
    -keepclassmembers class **$WhenMappings {
        <fields>;
    }

    #Ignore null checks at runtime
    -assumenosideeffects class kotlin.jvm.internal.Intrinsics {
        static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
    }
    #############
    # BottomBar (Needed to call methods via reflection to customize it)
    #############
    -keep class android.support.design.internal.** { *; }


    #############
    # WebViews
    #############
    # If your project uses WebView with JS, uncomment the following
    # and specify the fully qualified class name to the JavaScript interface
    # class:
    #-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    #   public *;
    #}
    -keep class android.support.v8.renderscript.** { *; }

    ########################################
    # External Libraries
    ########################################


    #############
    # Google Play Services
    #############
    -keep class com.google.android.gms.* {  *; }
    -dontwarn com.google.android.gms.**
    -keep public class com.google.vending.licensing.ILicensingService
    -keep public class com.android.vending.licensing.ILicensingService
    -dontnote **ILicensingService
    -dontnote com.google.android.gms.gcm.GcmListenerService
    -dontnote com.google.android.gms.**

    -dontwarn com.google.android.gms.ads.**
    #############
    # Android Support Lib
    #############
    -keep class android.support.design.widget.TextInputLayout { *; }
    #############
    # Firebase
    #############
    -dontnote com.google.firebase.**
    -dontwarn com.google.firebase.crash.**


    ##########
    # Android architecture components: Lifecycle ( https://issuetracker.google.com/issues/62113696 )
    ##########
    # LifecycleObserver's empty constructor is considered to be unused by proguard
    -keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
        <init>(...);
    }
    # ViewModel's empty constructor is considered to be unused by proguard
    -keepclassmembers class * extends android.arch.lifecycle.ViewModel {
        <init>(...);
    }
    # keep Lifecycle State and Event enums values
    -keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
    -keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
    # keep methods annotated with @OnLifecycleEvent even if they seem to be unused
    # (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
    -keepclassmembers class * {
        @android.arch.lifecycle.OnLifecycleEvent *;
    }
    #############
    # Retrofit
    #############
    -dontnote okio.**

    #############
    # HttpClient Legacy (Ignore) - org.apache.http legacy
    #############
    -dontnote android.net.http.*
    -dontnote org.apache.commons.codec.**
    -dontnote org.apache.http.**
    ##########
    # Glide
    ##########
    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
        **[] $VALUES;
        public *;
    }
    -dontnote com.bumptech.glide.**
    ##########
    # RxJava 2
    ##########

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
    #############
    # Stetho
    #############
    -dontnote com.facebook.stetho.**
    ##########
    # Crashlytics:
    # Adding this in to preserve line numbers so that the stack traces can be remapped
    ##########
    -renamesourcefileattribute SourceFile
    -keepattributes SourceFile,LineNumberTable


    ##########
    # Airbnb: Deep Linking Dispatch
    ##########

    -keep class com.airbnb.deeplinkdispatch.** { *; }
    -keepclasseswithmembers class * {
         @com.airbnb.deeplinkdispatch.DeepLink <methods>;
    }

    -keep @interface your.package.deeplinks.** { *; }
    -keepclasseswithmembers class * {
        @your.package.deeplinks.* <methods>;
    }

    -dontnote com.airbnb.deeplinkdispatch.**
    #############
    # Carousel View
    #############
    -dontnote com.synnapps.carouselview.**
    ##########
    # BlurView
    ##########
    -dontnote eightbitlab.com.blurview.**


    ##########
    # cwac-netsecurity
    ##########
    -keep class com.commonsware.cwac.netsecurity.** { *; }

    -keep class com.bibsindia.bibstraderpanel.model.** { *; }
    -keep class com.bibsindia.bibstraderpanel.model.catalogue.** { *; }
    -keep class com.bibsindia.bibstraderpanel.model.dashboard.** { *; }
    -keep class com.bibsindia.bibstraderpanel.model.list.** { *; }
    -keep class com.bibsindia.bibstraderpanel.model.order.** { *; }
    -keep class com.bibsindia.bibstraderpanel.model.orderDetails.** { *; }
    -keep class com.bibsindia.bibstraderpanel.model.placeOrder.** { *; }

    -keepattributes *Annotation*
    -keepattributes SourceFile,LineNumberTable
    -keep public class * extends java.lang.Exception
    #-keep class com.crashlytics.** { *; }
    #-dontwarn com.crashlytics.**

    -dontwarn android.databinding.**
    -keep class android.databinding.** { *; }

    -keep class com.android.vending.billing.**