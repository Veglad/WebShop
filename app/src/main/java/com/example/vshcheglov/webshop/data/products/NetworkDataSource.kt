package com.example.vshcheglov.webshop.data.products

import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
import com.example.vshcheglov.webshop.data.network.WebShopApi
import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single

class NetworkDataSource(private val productEntityDataMapper: ProductEntityDataMapper,
                        private val webShopApi: WebShopApi) {

    fun getAllDevices(): Single<List<Product>> = webShopApi.getDevices().map {
        productEntityDataMapper.mapFrom(it)
    }

    @Deprecated("Does not work")
    fun getDevice(id: Long): Single<Product> = webShopApi.getDevice(id).map {
        productEntityDataMapper.mapFrom(it)
    }

    fun getAllPromotionalDevices(): Single<List<Product>> = webShopApi.getPromotionalDevices().map {
        productEntityDataMapper.mapFrom(it)
    }
}