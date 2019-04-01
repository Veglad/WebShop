package com.example.vshcheglov.webshop

import android.app.Application
import com.example.vshcheglov.webshop.presentation.di.components.AppComponent
import com.example.vshcheglov.webshop.presentation.di.components.DaggerAppComponent
import com.example.vshcheglov.webshop.presentation.di.modules.*
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()

        appComponent = DaggerAppComponent.builder()
            .productNetworkModule(ProductNetworkModule())
            .appModule(AppModule(this))
            .mappersModule(MappersModule())
            .productStorageModule(ProductRepositoryModule())
            .userRepositoryModule(UserRepositoryModule())
            .build()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}