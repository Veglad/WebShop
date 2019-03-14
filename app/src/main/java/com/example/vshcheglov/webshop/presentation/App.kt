package com.example.vshcheglov.webshop.presentation

import android.app.Application
import com.example.vshcheglov.webshop.BuildConfig
import com.example.vshcheglov.webshop.data.products.NetworkDataSource
import com.example.vshcheglov.webshop.presentation.di.modules.AppModule
import com.example.vshcheglov.webshop.presentation.di.modules.NetworkModule
import com.example.vshcheglov.webshop.presentation.di.components.DaggerProductsComponent
import com.example.vshcheglov.webshop.presentation.di.components.ProductsComponent
import com.example.vshcheglov.webshop.presentation.di.modules.MainActivityModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class App : Application() {

    lateinit var productsComponent: ProductsComponent

    override fun onCreate() {
        super.onCreate()

        initTimber()

        val interceptor = HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
        val client =  OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(NetworkDataSource.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()


        productsComponent = DaggerProductsComponent.builder()
            .networkModule(NetworkModule(retrofit))
            .appModule(AppModule(this))
            .mainActivityModule(MainActivityModule())
            .build()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}