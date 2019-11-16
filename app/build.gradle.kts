import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("org.jlleitschuh.gradle.ktlint")
}

kapt {
    generateStubs = true
    arguments {
        arg("toothpick_registry_package_name", "com.github.rcd27")
    }
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.github.rcd27"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // The following argument makes the Android Test Orchestrator run its
        // "pm clear" command after each test invocation. This command ensures
        // that the app's state is completely cleared between tests.
        testInstrumentationRunnerArgument("clearPackageData", "true")
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            buildConfigField("String", "SERVER_URL", "")
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        create("dev") {
            isMinifyEnabled = false
            buildConfigField("String", "SERVER_URL", "")
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("staging") {
            isMinifyEnabled = false
            buildConfigField("String", "SERVER_URL", "")
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField("String", "SERVER_URL", "")
            applicationIdSuffix = ".release"
            versionNameSuffix = "-release"
        }
    }
    // FIXME: try to clean-up
    // Ugly but works. If try to move to lambdas call - doesn't work
    applicationVariants.all(object : Action<ApplicationVariant> {
        override fun execute(variant: ApplicationVariant) {
            variant.outputs.all(object : Action<BaseVariantOutput> {
                override fun execute(t: BaseVariantOutput) {
                    val output = t as BaseVariantOutputImpl
                    output.outputFileName =
                        "Shell-${variant.buildType.name}-v${defaultConfig.versionName}.apk"
                }
            })
        }
    })
    flavorDimensions("api")
    productFlavors {
        create("mock") {
            setDimension("api")
            versionNameSuffix = "-mockApi"
        }
        create("net") {
            setDimension("api")
            versionNameSuffix = "-netApi"
        }
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        unitTests.isIncludeAndroidResources = true
    }
    lintOptions.isAbortOnError = false
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.50")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.50")
    implementation("com.google.android.gms:play-services-vision:19.0.0")

    // UI
    implementation("com.google.android.material:material:1.2.0-alpha01")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.fragment:fragment:1.2.0-rc02")
    implementation("androidx.viewpager2:viewpager2:1.0.0-rc01")

    // RecyclerView extensions
    implementation("com.hannesdorfmann:adapterdelegates4:4.1.1")
    implementation("com.hannesdorfmann:adapterdelegates4-kotlin-dsl:4.1.1")
    implementation("com.hannesdorfmann:adapterdelegates4-kotlin-dsl-layoutcontainer:4.1.1")

    // This is for ViewModelProviders
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")

    // Network
    implementation("com.squareup.retrofit2:retrofit:2.6.2")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.4.0")

    // Dependency Injection: Toothpick
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:1.1.3")
    implementation("com.github.stephanenicolas.toothpick:toothpick-runtime:1.1.3")
    implementation("com.github.stephanenicolas.toothpick:smoothie:1.1.3")

    // TODO: Dependency Injection: Dagger

    // Extensions
    implementation("androidx.core:core-ktx:1.1.0")

    // RxJava
    implementation("io.reactivex.rxjava2:rxjava:2.2.13")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0")
    implementation("com.jakewharton.rxrelay2:rxrelay:2.1.0")

    // Background Service
    implementation("androidx.work:work-runtime-ktx:2.2.0")
    implementation("androidx.work:work-rxjava2:2.2.0")
    androidTestImplementation("androidx.work:work-testing:2.2.0")

    // Video Player
    implementation("com.google.android.exoplayer:exoplayer-core:2.7.3")
    implementation("com.google.android.exoplayer:exoplayer-dash:2.7.3")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.7.3")

    // Navigation
    implementation("androidx.navigation:navigation-fragment:2.2.0-rc02")
    implementation("androidx.navigation:navigation-ui:2.2.0-rc02")

    // Load images
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.github.chrisbanes:PhotoView:2.0.0")

    // Input Mask
    implementation("ru.tinkoff.decoro:decoro:1.3.5")

    // Logging
    implementation("com.jakewharton.timber:timber:4.7.1")
    testImplementation("com.squareup.okhttp3:logging-interceptor:3.10.0")

    // LeakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.0-alpha-1")

    // Testing
    testImplementation("junit:junit:4.12")
    testImplementation("io.mockk:mockk:1.9.1")
    testImplementation("com.google.truth:truth:1.0")

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test:core:1.2.0")
    androidTestImplementation("androidx.test:core-ktx:1.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.1")
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("com.google.truth:truth:1.0")
    androidTestUtil("androidx.test:orchestrator:1.2.0")
}

// TODO: clean up
configurations {
    all {
        resolutionStrategy {
            force("com.squareup.okhttp3:okhttp:3.11.0")
            force("com.google.guava:listenablefuture:1.0")
            force("androidx.test:core:1.2.0")
        }
    }
}

ktlint {
    android.set(true)
    ignoreFailures.set(false) // We need this if we want to keep the code base clean
}