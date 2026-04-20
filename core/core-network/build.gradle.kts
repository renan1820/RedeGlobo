plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.apollo)
}

android {
    namespace = "com.gitproject.redeglobo.network"
    compileSdk = 36

    defaultConfig { minSdk = 24 }
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

apollo {
    service("rickandmorty") {
        packageName.set("com.gitproject.redeglobo.graphql")
        schemaFiles.from("src/main/graphql/schema.graphqls")
        srcDir("src/main/graphql")
        generateOptionalOperationVariables.set(false)
    }
}

dependencies {
    implementation(project(":core:core-common"))
    api(libs.apollo.runtime)
    api(libs.apollo.rx3)
    api(libs.apollo.normalized.cache)
    implementation(libs.apollo.normalized.cache.sqlite)
    implementation(libs.bundles.rxjava)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.rx3)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    testImplementation(libs.bundles.testing.unit)
}
