package com.example.vshcheglov.webshop.data.products

import com.example.vshcheglov.webshop.data.enteties.AllProducts
import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single

class ProductRepository(private val networkDataSource: NetworkDataSource) {

    suspend fun getProducts() = networkDataSource.getProducts()

    suspend fun getPromotionalProducts() = networkDataSource.getPromotionalProducts()

    suspend fun getAllProducts() = networkDataSource.getAllProducts()
}