package com.example.vshcheglov.webshop.data

import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WebShopApi {

    @GET("/api/DeviceData")
    fun getAllDevices(): Single<List<Product>>

    @GET("/api/DeviceData/{deviceId}")
    fun getDevice(@Path("deviceId") id: Long): Single<Product>

    @GET("/api/DeviceData")
    fun getAllPromotionalDevices(): Single<List<Product>>

}