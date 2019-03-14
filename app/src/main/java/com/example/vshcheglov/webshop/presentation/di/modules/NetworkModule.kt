package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
import com.example.vshcheglov.webshop.data.network.WebShopApi
import com.example.vshcheglov.webshop.data.products.NetworkDataSource
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideShopApi(): WebShopApi {
        val interceptor = HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(NetworkDataSource.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(WebShopApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkProductMapper() = ProductEntityDataMapper()
}