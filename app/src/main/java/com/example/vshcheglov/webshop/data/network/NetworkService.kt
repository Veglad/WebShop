package com.example.vshcheglov.webshop.data.network

import android.content.Context
import com.example.vshcheglov.webshop.data.products.NetworkDataSource
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.example.vshcheglov.webshop.presentation.di.modules.NetworkModule
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

object NetworkService {
    private const val MAX_LIFE = 60 * 60 * 24 * 30

    fun createRetrofit(context: Context) : Retrofit {
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
                val maxStale = MAX_LIFE
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

        return Retrofit.Builder()
            .baseUrl(NetworkDataSource.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}