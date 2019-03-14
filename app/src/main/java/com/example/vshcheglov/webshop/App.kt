package com.example.vshcheglov.webshop

import android.app.Application
import com.example.vshcheglov.webshop.presentation.di.modules.AppModule
import com.example.vshcheglov.webshop.presentation.di.modules.NetworkModule
import com.example.vshcheglov.webshop.presentation.di.components.AppComponent
import com.example.vshcheglov.webshop.presentation.di.components.DaggerAppComponent
import com.example.vshcheglov.webshop.presentation.di.modules.BasketActivityModule
import com.example.vshcheglov.webshop.presentation.di.modules.StorageModule
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()

        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .appModule(AppModule(this))
            .basketActivityModule(BasketActivityModule())
            .storageModule(StorageModule())
            .build()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}