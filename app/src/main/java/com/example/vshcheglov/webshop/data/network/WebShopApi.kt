package com.example.vshcheglov.webshop.data.network

import com.example.vshcheglov.webshop.data.enteties.ProductEntity
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface WebShopApi {

    @GET("/products")
    fun getProductsAsync(): Deferred<List<ProductEntity>>

    @Deprecated("Does not work")
    @GET("/api/DeviceData/{deviceId}")
    fun getDeviceAsync(@Path("deviceId") id: Long): Deferred<ProductEntity>

    @GET("/products")
    fun getPromotionalProductsAsync(): Deferred<List<ProductEntity>>

}