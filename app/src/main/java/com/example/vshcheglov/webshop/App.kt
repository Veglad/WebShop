package com.example.vshcheglov.webshop

import android.app.Application
import com.example.vshcheglov.webshop.presentation.di.components.AppComponent
import com.example.vshcheglov.webshop.presentation.di.components.DaggerAppComponent
import com.example.vshcheglov.webshop.presentation.di.modules.*
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber



class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent

        private const val KEY_ALIAS = "key_for_pin"
        private const val KEYSTORE_NAME = "AndroidKeyStore"
        private const val CIPHER_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initRealmConfiguration()

        appComponent = DaggerAppComponent.builder()
            .productNetworkModule(ProductNetworkModule())
            .productStorageModule(ProductStorageModule())
            .userStorageModule(UserStorageModule())
            .appModule(AppModule(this))
            .mappersModule(MappersModule())
            .dataProviderModule(DataProviderModule())
            .encryptorModule(EncryptorModule(KEY_ALIAS, KEYSTORE_NAME, CIPHER_TRANSFORMATION))
            .build()
    }

    private fun initRealmConfiguration() {
        Realm.init(this)
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}