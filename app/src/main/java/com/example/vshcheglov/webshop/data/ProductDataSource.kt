package com.example.vshcheglov.webshop.data

import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single

interface ProductDataSource {
    fun getAllDevices(): Single<List<Product>>

    fun getDevice(id: Long): Single<Product>

    fun getAllPromotionalDevices(): Single<List<Product>>
}