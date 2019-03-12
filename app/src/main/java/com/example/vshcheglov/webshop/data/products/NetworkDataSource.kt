package com.example.vshcheglov.webshop.data.products

import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
import com.example.vshcheglov.webshop.data.network.WebShopApi
import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkDataSource(private val productEntityDataMapper: ProductEntityDataMapper,
                        private val retrofit: Retrofit) {
    private val webShopApi: WebShopApi by lazy {
        retrofit.create(WebShopApi::class.java)
    }

    fun getAllDevices(): Single<List<Product>> = webShopApi.getDevices().map {
        productEntityDataMapper.transform(it)
    }

    @Deprecated("Does not work")
    fun getDevice(id: Long): Single<Product> = webShopApi.getDevice(id).map {
        productEntityDataMapper.transform(it)
    }

    fun getAllPromotionalDevices(): Single<List<Product>> = webShopApi.getPromotionalDevices().map {
        productEntityDataMapper.transform(it)
    }
}