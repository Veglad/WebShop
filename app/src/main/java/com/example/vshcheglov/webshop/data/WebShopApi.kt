package com.example.vshcheglov.webshop.data

import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface WebShopApi {

    @GET("/products")
    fun getAllDevices(): Single<List<Product>>

    @Deprecated("Does not work")
    @GET("/api/DeviceData/{deviceId}")
    fun getDevice(@Path("deviceId") id: Long): Single<Product>

    @GET("/products")
    fun getAllPromotionalDevices(): Single<List<Product>>

}