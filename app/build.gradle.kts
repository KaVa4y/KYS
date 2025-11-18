plugins {
    id 'com.android.application'
}

android {
    namespace 'com.kava.kys'
    compileSdk 35

    defaultConfig {
        applicationId "com.kava.kys"
        minSdk 29
        targetSdk 35
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
                targetCompatibility JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding true
    }
}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.7.0'
    implementation 'com.google.android.material:material:1.12.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.8.6'
    implementation 'androidx.lifecycle:lifecycle-livedata:2.8.6'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'

    // Room
    implementation "androidx.room:room-runtime:2.6.1"
    annotationProcessor "androidx.room:room-compiler:2.6.1"

    // WorkManager
    implementation "androidx.work:work-runtime:2.9.1"
}