package com.example.vshcheglov.webshop.presentation.di.modules

import android.content.Context
import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
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

    companion object {
        const val _30_DAYS_IN_MILLISECONDS = 60 * 60 * 24 * 30
    }

    @Singleton
    @Provides
    fun provideShopApi(context: Context): WebShopApi {
        val loggingInterceptor = HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }

        val onlineInterceptor = Interceptor { chain ->
            chain.proceed(chain.request())
                .newBuilder()
                .removeHeader("Pragma")
                .build()
        }

        val offlineInterceptor = Interceptor { chain ->
            var request = chain.request()

            if (!context.isNetworkAvailable()) {
                val maxStale = _30_DAYS_IN_MILLISECONDS
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build()
            }

            chain.proceed(request)
        }

        val cache = Cache(File(context.cacheDir, "http-cache"), 10 * 1024 * 1024)

        val client = OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(offlineInterceptor)
            .addInterceptor(onlineInterceptor)
            .cache(cache)
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