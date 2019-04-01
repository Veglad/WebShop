package com.example.vshcheglov.webshop.data.products.network

import com.example.vshcheglov.webshop.data.enteties.ProductNetwork
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface WebShopApi {

    @GET("/products")
    fun getProductsAsync(): Deferred<List<ProductNetwork>>

    @Deprecated("Does not work")
    @GET("/api/DeviceData/{deviceId}")
    fun getDeviceAsync(@Path("deviceId") id: Long): Deferred<ProductNetwork>

    @GET("/products")
    fun getPromotionalProductsAsync(): Deferred<List<ProductNetwork>>

}