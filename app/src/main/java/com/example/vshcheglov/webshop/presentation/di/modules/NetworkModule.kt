package com.example.vshcheglov.webshop.presentation.di.modules

import android.content.Context
import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
import com.example.vshcheglov.webshop.data.network.NetworkService
import com.example.vshcheglov.webshop.data.network.WebShopApi
import com.example.vshcheglov.webshop.data.products.NetworkDataSource
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class NetworkModule {
    @Singleton
    @Provides
    fun provideShopApi(context: Context): WebShopApi {
        val retrofit = NetworkService.createRetrofit(context)
        return retrofit.create(WebShopApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkProductMapper() = ProductEntityDataMapper()
}