package com.example.vshcheglov.webshop.data

import com.example.vshcheglov.webshop.data.network.NetworkDataSource
import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single

object ProductRepository : IProductDataSource {

    override fun getAllDevices(): Single<List<Product>> {
        return NetworkDataSource.getAllDevices()
    }

    override fun getDevice(id: Long): Single<Product> {
        return NetworkDataSource.getDevice(id)
    }

    override fun getAllPromotionalDevices(): Single<List<Product>> {
        return NetworkDataSource.getAllPromotionalDevices()
    }

}