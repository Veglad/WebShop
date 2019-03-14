package com.example.vshcheglov.webshop

import android.app.Application
import com.example.vshcheglov.webshop.presentation.di.modules.AppModule
import com.example.vshcheglov.webshop.presentation.di.modules.NetworkModule
import com.example.vshcheglov.webshop.presentation.di.components.AppComponent
import com.example.vshcheglov.webshop.presentation.di.components.DaggerAppComponent
import com.example.vshcheglov.webshop.presentation.di.modules.StorageModule
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var productsComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()

        productsComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .appModule(AppModule(this))
            .storageModule(StorageModule())
            .build()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}