package com.example.vshcheglov.webshop.data.products

import com.example.vshcheglov.webshop.data.ProductDataSource
import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single

class ProductRepository(private val networkDataSource: NetworkDataSource) : ProductDataSource {
    override fun getAllDevices(): Single<List<Product>> {
        return networkDataSource.getAllDevices()
    }

    @Deprecated("Does not work")
    override fun getDevice(id: Long): Single<Product> {
        return networkDataSource.getDevice(id)
    }

    override fun getAllPromotionalDevices(): Single<List<Product>> {
        return networkDataSource.getAllPromotionalDevices()
    }

}