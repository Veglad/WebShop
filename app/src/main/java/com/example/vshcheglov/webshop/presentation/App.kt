package com.example.vshcheglov.webshop.presentation

import android.app.Application
import com.example.vshcheglov.webshop.presentation.di.modules.AppModule
import com.example.vshcheglov.webshop.presentation.di.modules.NetworkModule
import com.example.vshcheglov.webshop.presentation.di.products.DaggerProductsComponent
import com.example.vshcheglov.webshop.presentation.di.products.ProductsComponent

class App : Application() {

    lateinit var productsComponent: ProductsComponent

    override fun onCreate() {
        super.onCreate()

        productsComponent = DaggerProductsComponent.builder()
            .networkModule(NetworkModule("https://us-central1-webshop-58013.cloudfunctions.net"))
            .appModule(AppModule(this))
            .build()
    }
}