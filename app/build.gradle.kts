plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.quizappv3"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quizappv3"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

        // Datastore for nøkkelverdier: Innstillinger
    implementation ("androidx.datastore:datastore-preferences:1.1.7")

        // optional - RxJava2 support
    implementation ("androidx.datastore:datastore-preferences-rxjava2:1.1.7")

        // optional - RxJava3 support
    implementation ("androidx.datastore:datastore-preferences-rxjava3:1.1.7")

    // Room
    val room_version = "2.7.1"

    // Obligatorisk for Room
    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor ("androidx.room:room-compiler:$room_version")

    // Hvis du bruker RxJava3 for databasen
    implementation ("androidx.room:room-rxjava3:$room_version")

    // (valgfritt, men nyttig hvis du skal teste Room senere)
    testImplementation ("androidx.room:room-testing:$room_version")


    // forsøk på musikk over alt
    implementation ("androidx.lifecycle:lifecycle-process:2.9.0")

}