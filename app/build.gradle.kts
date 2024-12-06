

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.runtracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.runtracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        kapt {
            correctErrorTypes = true
            arguments {
                arg(
                    "room.schemaLocation",
                    "$projectDir/schemas"
                )
            }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

object Apps {
    const val compileSdk = 34
    const val minSdk = 21
    const val targetSdk = 34
    const val versionCode = 20
    const val versionName = "2.0.3"
    const val gradle_version = "4.2.0"
}

object Libs {

    /* ----------------------------------- Core Libs -------------------------------------------- */

    object Core {
        private const val appcompat_version = "1.1.0"
        const val kotlin_stdlib_version = "1.8.0"
        private const val core_ktx_verstion = "1.2.0"
        private const val material_version = "1.2.0"
        private const val constraintlayout_version = "2.1.1"
        private const val vectordrawable_version = "1.0.1"
        private const val multidex_version = "1.0.3"


        const val appcompat = "androidx.appcompat:appcompat:${appcompat_version}"
        const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${kotlin_stdlib_version}"
        const val core_ktx = "androidx.core:core-ktx:${core_ktx_verstion}"
        const val material = "com.google.android.material:material:${material_version}"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:${constraintlayout_version}"
        const val vectordrawable = "androidx.vectordrawable:vectordrawable:${vectordrawable_version}"
        const val multidex = "com.android.support:multidex:${multidex_version}"
    }


    /* ------------------------------------ Dagger ---------------------------------------------- */

    object Dagger {
        private const val hilt_version = "2.48"
        private const val hilt_navigation_fragment_version = "1.0.0"
        private const val hilt_work_manager_version = "1.0.0"

        const val hilt_android = "com.google.dagger:hilt-android:2.48"
        const val hilt_android_compiler = "com.google.dagger:hilt-android-compiler:2.48"
        const val hilt_navigation_fragment = "androidx.hilt:hilt-navigation-fragment:1.0.0"
        const val hilt_work_manager = "androidx.hilt:hilt-work:1.0.0"
        const val hilt_compiler = "androidx.hilt:hilt-compiler:1.0.0"

    }


    /* ---------------------------------- Navigation -------------------------------------------- */

    object Navigation {
        const val navigation_version = "2.7.3"

        const val navigation_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${navigation_version}"
        const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${navigation_version}"
    }

    /* ------------------------------------- Room ----------------------------------------------- */
    object Room {
        const val room = "2.4.0-alpha04"

        const val room_runtime = "androidx.room:room-runtime:${room}"
        const val room_ktx = "androidx.room:room-ktx:2.4.0-alpha04"
        const val room_annotation_compiler = "androidx.room:room-compiler:2.4.0-alpha04"
    }

    /* --------------------------------- Architecture ------------------------------------------- */
    object Architecture {
        private const val lifecycle_extensions_version = "2.5.0"

        const val lifecycle_livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${lifecycle_extensions_version}"
        const val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycle_extensions_version}"
        const val lifecycle_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:${lifecycle_extensions_version}"
    }

    /* ----------------------------------- Firebase --------------------------------------------- */

    object Firebase {
        private const val analytics_version = "17.4.4"
        private const val firebaseMessagingVersion = "20.1.7"
        private const val firebaseAuth = "20.0.4"
        private const val firebase_crashlytics_version = "17.1.1"

        const val firebase_analytics = "com.google.firebase:firebase-analytics:$analytics_version"
        const val firebase_messaging = "com.google.firebase:firebase-messaging"
        const val firebase_authentication = "com.google.firebase:firebase-auth:$firebaseAuth"
        const val firebase_crashlytics = "com.google.firebase:firebase-crashlytics:$firebase_crashlytics_version"
        const val firebase_analytics_ktx=  "com.google.firebase:firebase-analytics-ktx:21.1.1"

    }

    /* ------------------------------------ Testing --------------------------------------------- */
    object Test {
        private const val junit_version = "4.12"
        private const val mockito_kotlin_version = "1.5.0"
        private const val mockito_android_version = "2.8.47"
        private const val junit_ext_version = "1.1.3"
        private const val expresso_core_version = "3.1.1"

        const val junit_dep = "junit:junit:${junit_version}"
        const val mockito_kotlin = "com.nhaarman:mockito-kotlin:${mockito_kotlin_version}"
        const val mockito_android = "org.mockito:mockito-android:${mockito_android_version}"
        const val junit_ext = "androidx.test.ext:junit:${junit_ext_version}"
        const val expresso_core = "androidx.test.espresso:espresso-core:${expresso_core_version}"
    }

    /* ------------------------------ Retrofit & Network  --------------------------------------- */
    object Retrofit {
        private const val retrofit_version = "2.8.1"
        private const val logging_interceptor_version = "4.9.1"

        const val retrofit = "com.squareup.retrofit2:retrofit:${retrofit_version}"
        const val converter_gson = "com.squareup.retrofit2:converter-gson:${retrofit_version}"
        const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${logging_interceptor_version}"
        const val picasso= "com.squareup.picasso:picasso:2.5.2"
    }

    /* ----------------------------- Google Play Services --------------------------------------- */
    object Play {

        private const val play_services_maps_version = "17.0.1"
        private const val play_services_location_version = "18.0.0"
        private const val play_services_places_version = "16.1.0"
        private const val play_services_auth_version = "19.2.0"
        private const val places_version = "2.2.0"
        private const val play_core_version = "1.10.0"
        private const val play_core_ktx_version = "1.8.1"
        private const val android_maps_util_version = "2.2.3"
        private const val android_maps_util_ktx_version = "3.2.0"


        const val play_services_maps = "com.google.android.gms:play-services-maps:${play_services_maps_version}"
        const val play_services_location = "com.google.android.gms:play-services-location:$play_services_location_version"
        const val play_services_places = "com.google.android.gms:play-services-places:$play_services_places_version"
        const val play_services_auth = "com.google.android.gms:play-services-auth:${play_services_auth_version}"
        const val places = "com.google.android.libraries.places:places:${places_version}"
        //        const val play_core = "com.google.android.play:core:${play_core_version}"
//        const val play_core_ktx = "com.google.android.play:core-ktx:${play_core_ktx_version}"
        const val android_maps_util = "com.google.maps.android:android-maps-utils:${android_maps_util_version}"
        const val android_maps_util_ktx = "com.google.maps.android:maps-utils-ktx:${android_maps_util_ktx_version}"
    }

    /* ----------------------------------- AndroidX --------------------------------------------- */
    object AndroidX {
        private const val browser_version = "1.0.0"
        private const val paging_runtime_version = "3.0.0-alpha02"

        const val browser = "androidx.browser:browser:$browser_version"
        const val paging_runtime = "androidx.paging:paging-runtime:$paging_runtime_version"
    }

    /* --------------------------------- Facebook Login ----------------------------------------- */
    private const val facebook_login_version = "11.1.0"
    const val facebook_login = "com.facebook.android:facebook-login:${facebook_login_version}"

    /* ------------------------------------- Epoxy ---------------------------------------------- */
    private const val epoxy_version = "4.4.2"
    const val epoxy = "com.airbnb.android:epoxy:$epoxy_version"
    const val epoxy_processor = "com.airbnb.android:epoxy-processor:$epoxy_version"

    /* ------------------------------------- Glide ---------------------------------------------- */
    private const val glide_version = "4.10.0"
    const val glide = "com.github.bumptech.glide:glide:${glide_version}"

    /* ------------------------------------ FlexBox --------------------------------------------- */
    private const val flexbox_version = "2.0.1"
    const val flexbox = "com.google.android:flexbox:$flexbox_version"

    /* ------------------------------ Sectioned RV Adapter -------------------------------------- */
    private const val sectioned_recyclerview_adapter_version = "3.1.0"

    const val sectioned_recycler_adapter =
        "io.github.luizgrp.sectionedrecyclerviewadapter:sectionedrecyclerviewadapter:" +
                sectioned_recyclerview_adapter_version

    /* ------------------------------------- Timber --------------------------------------------- */
    private const val timber_version = "4.7.1"
    const val timber = "com.jakewharton.timber:timber:$timber_version"

    /* -------------------------------- Circle ImageView ---------------------------------------- */
    private const val hdodenhof_circle_image_version = "2.2.0"
    const val hdodenhof_circle_image = "de.hdodenhof:circleimageview:$hdodenhof_circle_image_version"

    /* ------------------------------------ Debug DB -------------------------------------------- */
    private const val debug_db_version = "1.0.6"
    const val debug_db = "com.amitshekhar.android:debug-db:$debug_db_version" //this was made private

    /* ------------------------------------- Lottie --------------------------------------------- */
    private const val lottie_version = "3.4.0"
    const val lottie = "com.airbnb.android:lottie:$lottie_version"

    /* ------------------------------------- shimmer effect --------------------------------------------- */
    private const val shimmer_version = "0.5.0"
    const val shimmer = "com.facebook.shimmer:shimmer:$shimmer_version"

    /* ------------------------------------- PhotoView --------------------------------------------- */
    private const val photo_view_version = "2.0.0"
    const val photo_view = "com.github.chrisbanes:PhotoView:$photo_view_version"


    /* ------------------------------------- Stetho --------------------------------------------- */
    private const val stetho_version = "1.6.0"
    const val stetho = "com.facebook.stetho:stetho:$stetho_version"
    const val stetho_okhttp = "com.facebook.stetho:stetho-okhttp3:$stetho_version"

    /* ----------------------------------- Joda time ------------------------------------------- */
    private const val joda_version = "2.10.14"
    const val joda_time = "joda-time:joda-time:${joda_version}"


    /* ---------------------------------- Work manager ------------------------------------------ */
    private const val work_manager_version = "2.7.1"
    const val work_runtime_ktx = "androidx.work:work-runtime-ktx:$work_manager_version"

    /* ----------------------------------   CameraX   ------------------------------------------ */
    private const val camerax_version = "1.1.0-beta01"
    const val camera_core = "androidx.camera:camera-core:${camerax_version}"
    const val camera_camera2 = "androidx.camera:camera-camera2:${camerax_version}"

    const val camera_lifecycle = "androidx.camera:camera-lifecycle:${camerax_version}"
    const val camera_video = "androidx.camera:camera-video:${camerax_version}"
    const val camera_view = "androidx.camera:camera-view:${camerax_version}"
    const val camera_extensions = "androidx.camera:camera-extensions:${camerax_version}"

    /* ----------------------------------   App update   ------------------------------------------ */
    const val update_app =  "com.google.android.play:app-update:2.0.1"
    const val update_app_ktx =  "com.google.android.play:app-update-ktx:2.0.1"

}


    dependencies {
        // Core Libraries
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.fragment:fragment-ktx:1.8.1")

        // Testing
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

        // Navigation Components
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
        implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

        // Lifecycle Components
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

        // Room (Database)
        implementation("androidx.room:room-runtime:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")
        kapt("androidx.room:room-compiler:2.6.1")

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

        // Glide (Image Loading)
        implementation("com.github.bumptech.glide:glide:4.15.1")
        kapt("com.github.bumptech.glide:compiler:4.15.1")

        // Google Maps & Location Services
        implementation("com.google.android.gms:play-services-location:21.0.1")
        implementation("com.google.android.gms:play-services-maps:18.1.0")

        // Dagger - Hilt
        implementation("com.google.dagger:hilt-android:2.50")
        kapt("com.google.dagger:hilt-android-compiler:2.50")

        // Logging
        implementation("com.jakewharton.timber:timber:5.0.1")

        // Permissions
        implementation("pub.devrel:easypermissions:3.0.0")
    }

