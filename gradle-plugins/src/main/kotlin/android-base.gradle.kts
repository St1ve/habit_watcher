import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    // FIXME: Use toml versions
    compileSdkVersion(34)
    //    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = 21
//        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = 34

        // FIXME: Uncomment
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables {
//            useSupportLibrary = true
//        }

//        targetSdk = libs.versions.targetSdk.get().toInt()
    }
}
