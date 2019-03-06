package com.example.vshcheglov.webshop.data.network

import com.example.vshcheglov.webshop.data.IProductDataSource
import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkDataSource : IProductDataSource {
    private const val BASE_URL = "https://us-central1-webshop-58013.cloudfunctions.net"

    private val webShopApi: WebShopApi by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        retrofit.create(WebShopApi::class.java)
    }

    override fun getAllDevices(): Single<List<Product>> = webShopApi.getDevices().map {
        ProductEntityDataMapper.transform(it)
    }

    @Deprecated("Does not work")
    override fun getDevice(id: Long): Single<Product> = webShopApi.getDevice(id).map {
        ProductEntityDataMapper.transform(it)
    }

    override fun getAllPromotionalDevices(): Single<List<Product>> = webShopApi.getPromotionalDevices().map {
        ProductEntityDataMapper.transform(it)
    }
}