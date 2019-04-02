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
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initRealmConfiguration()

        appComponent = DaggerAppComponent.builder()
            .productNetworkModule(ProductNetworkModule())
            .appModule(AppModule(this))
            .mappersModule(MappersModule())
            .dataProviderModule(DataProviderModule())
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