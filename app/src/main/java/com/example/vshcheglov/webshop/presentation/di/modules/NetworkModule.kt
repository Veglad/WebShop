package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.data.network.WebShopApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NetworkModule(private val retrofit: Retrofit) {
    @Singleton
    @Provides
    fun provideShopApi() = retrofit.create(WebShopApi::class.java)
}