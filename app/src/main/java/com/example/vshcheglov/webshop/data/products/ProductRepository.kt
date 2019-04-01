package com.example.vshcheglov.webshop.data.products

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.mappers.RealmAllProductsAllProductsMapper
import com.example.vshcheglov.webshop.data.enteties.mappers.RealmProductProductMapper
import javax.inject.Inject

class ProductRepository {

    @Inject
    lateinit var networkDataSource: ProductNetworkDataSource
    @Inject
    lateinit var productProductMapper: RealmProductProductMapper
    @Inject
    lateinit var allProductsMapper: RealmAllProductsAllProductsMapper

    init {
        App.appComponent.inject(this)
    }

    suspend fun getProducts() = networkDataSource.getProducts()

    suspend fun getPromotionalProducts() = networkDataSource.getPromotionalProducts()

    suspend fun getAllProducts() = networkDataSource.getAllProducts()
}