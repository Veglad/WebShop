package com.example.vshcheglov.webshop.data.products

import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single

class ProductRepository(private val networkDataSource: NetworkDataSource) {

    fun getAllDevices(): Single<List<Product>> {
        return networkDataSource.getAllDevices()
    }

    @Deprecated("Does not work")
    fun getDevice(id: Long): Single<Product> {
        return networkDataSource.getDevice(id)
    }

    fun getAllPromotionalDevices(): Single<List<Product>> {
        return networkDataSource.getAllPromotionalDevices()
    }

}