package com.example.vshcheglov.webshop.data

import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WebShopApi {

    @GET("/api/DeviceData")
    fun getAllDevices(): Single<List<Product>>

    @GET("/api/DeviceData/{id}")
    fun getDevice(@Path("id") id: Long): Single<Product>

    @POST("/api/DeviceData")
    fun getAllPromotionalDevices(): Single<List<Product>>

}