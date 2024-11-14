plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    /*
    added plugins
     */
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.dokka")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.dermatoai"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.dermatoai"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["appAuthRedirectScheme"] = "com.googleusercontent.apps.dermato_ai"

        buildConfigField(
            "String",
            "CLIENT_ID",
            "\"clientId\""
        )
        buildConfigField(
            "String",
            "WEB_CLIENT_ID",
            "\"webClientId\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    /*
    added library
    - room
    - retrofit
    - hilt
     */
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.play.services.auth)
    implementation(libs.googleid)
}
/*
configure dokka
 */
tasks.register("cleanDocs") {
    doLast {
        val docsDir = rootDir.resolve("docs")
        if (docsDir.exists()) {
            delete(docsDir)
        }
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("docs"))

    dokkaSourceSets {
        named("main") {
            moduleName.set("Dermato.AI")
            suppressInheritedMembers.set(true)
            includeNonPublic.set(false)
        }
    }

}