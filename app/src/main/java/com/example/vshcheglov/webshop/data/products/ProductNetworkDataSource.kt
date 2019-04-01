package com.example.vshcheglov.webshop.data.products

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.AllProductsEntity
import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
import com.example.vshcheglov.webshop.data.products.network.WebShopApi
import javax.inject.Inject

class ProductNetworkDataSource {

    @Inject lateinit var productEntityDataMapper: ProductEntityDataMapper
    @Inject lateinit var webShopApi: WebShopApi

    companion object {
        const val BASE_URL = "https://us-central1-webshop-58013.cloudfunctions.net"
    }

    init {
        App.appComponent.inject(this)
    }

    suspend fun getProducts() =
        productEntityDataMapper.map(webShopApi.getProductsAsync().await())

    suspend fun getPromotionalProducts() =
        productEntityDataMapper.map(webShopApi.getPromotionalProductsAsync().await())

    suspend fun getAllProducts(): AllProductsEntity {
        val productsDeferred = webShopApi.getProductsAsync()
        val promotionalProductsDeferred = webShopApi.getPromotionalProductsAsync()

        val products = productEntityDataMapper.map(productsDeferred.await())
        val promotionalProducts = productEntityDataMapper.map(promotionalProductsDeferred.await())

        return AllProductsEntity(products, promotionalProducts)
    }
}