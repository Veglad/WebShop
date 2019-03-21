package com.example.vshcheglov.webshop.data.products

import com.example.vshcheglov.webshop.App
import javax.inject.Inject

class ProductRepository {

    @Inject
    lateinit var networkDataSource: NetworkDataSource

    init {
        App.appComponent.inject(this)
    }

    suspend fun getProducts() = networkDataSource.getProducts()

    suspend fun getPromotionalProducts() = networkDataSource.getPromotionalProducts()

    suspend fun getAllProducts() = networkDataSource.getAllProducts()
}