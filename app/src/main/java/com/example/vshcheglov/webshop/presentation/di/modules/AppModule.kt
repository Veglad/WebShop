package com.example.vshcheglov.webshop.presentation.di.modules

import android.app.Application
import android.content.Context
import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
import com.example.vshcheglov.webshop.data.network.WebShopApi
import com.example.vshcheglov.webshop.data.products.NetworkDataSource
import com.example.vshcheglov.webshop.data.products.ProductRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class AppModule(private val applicationContext: Context) {

    @Provides
    @Singleton
    fun providesApplicationContext() = applicationContext

    @Provides
    @Singleton
    fun providesRepository(webShopApi: WebShopApi, mapper: ProductEntityDataMapper) =
        ProductRepository(NetworkDataSource(mapper, webShopApi))
}