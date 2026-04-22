plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.gitproject.redeglobo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.gitproject.redeglobo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.gitproject.redeglobo.HiltTestRunner"
    }

    flavorDimensions += "platform"

    productFlavors {
        create("mobile") {
            dimension = "platform"
            applicationIdSuffix = ".mobile"
            versionNameSuffix = "-mobile"
            buildConfigField("Boolean", "IS_TV", "false")
        }
        create("tv") {
            dimension = "platform"
            applicationIdSuffix = ".tv"
            versionNameSuffix = "-tv"
            buildConfigField("Boolean", "IS_TV", "true")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    sourceSets {
        getByName("mobile") {
            java.srcDirs("src/mobile/java")
        }
        getByName("tv") {
            java.srcDirs("src/tv/java")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-ui"))
    implementation(project(":data:data-content"))
    implementation(project(":feature:feature-home"))
    implementation(project(":feature:feature-detail"))
    implementation(project(":feature:feature-search"))
    implementation(project(":feature:feature-player"))
    implementation(project(":feature:feature-login"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.core)
    implementation(libs.bundles.lifecycle)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.navigation.compose)
    implementation(libs.bundles.rxjava)

    "tvImplementation"(libs.compose.tv.material)

    testImplementation(libs.junit4)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.testing.android)
    androidTestImplementation(libs.compose.ui.test.manifest)
    kspAndroidTest(libs.hilt.compiler)
    debugImplementation(libs.compose.ui.tooling)
}
