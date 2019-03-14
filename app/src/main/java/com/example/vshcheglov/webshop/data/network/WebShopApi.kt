package com.example.vshcheglov.webshop.data.network

import com.example.vshcheglov.webshop.data.enteties.ProductEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface WebShopApi {

    @GET("/products")
    fun getDevices(): Single<List<ProductEntity>>

    @Deprecated("Does not work")
    @GET("/api/DeviceData/{deviceId}")
    fun getDevice(@Path("deviceId") id: Long): Single<ProductEntity>

    @GET("/products")
    fun getPromotionalDevices(): Single<List<ProductEntity>>

}