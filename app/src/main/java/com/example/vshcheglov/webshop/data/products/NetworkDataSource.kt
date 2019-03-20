package com.example.vshcheglov.webshop.data.products

import com.example.vshcheglov.webshop.data.enteties.AllProducts
import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
import com.example.vshcheglov.webshop.data.network.WebShopApi

class NetworkDataSource(private val productEntityDataMapper: ProductEntityDataMapper,
                        private val webShopApi: WebShopApi) {

    companion object {
        const val BASE_URL = "https://us-central1-webshop-58013.cloudfunctions.net"
    }

    suspend fun getProducts() =
        productEntityDataMapper.mapFrom(webShopApi.getProductsAsync().await())

    suspend fun getPromotionalProducts() =
        productEntityDataMapper.mapFrom(webShopApi.getPromotionalProductsAsync().await())

    suspend fun getAllProducts(): AllProducts {
        val productsDeferred = webShopApi.getProductsAsync()
        val promotionalProductsDeferred = webShopApi.getPromotionalProductsAsync()

        val products = productEntityDataMapper.mapFrom(productsDeferred.await())
        val promotionalProducts = productEntityDataMapper.mapFrom(promotionalProductsDeferred.await())

        return AllProducts(products, promotionalProducts)
    }
}