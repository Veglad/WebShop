package com.example.vshcheglov.webshop.data

import com.example.vshcheglov.webshop.domain.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WebShopApi {

    @GET("/api/DeviceData")
    fun getAllDevices(): Call<List<Product>>

    @GET("/api/DeviceData/{id}")
    fun getDevice(@Path("id") id: Long): Call<Product>

    @POST("/api/DeviceData")
    fun getAllPromotionalDevices(): Call<List<Product>>

}