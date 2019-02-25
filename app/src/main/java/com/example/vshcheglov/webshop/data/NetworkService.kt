package com.example.vshcheglov.webshop.data

import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkService : DataSource{
    private const val BASE_URL = """http://multiflexersshop.azurewebsites.net"""

    private var webShopApi: WebShopApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        webShopApi = retrofit.create(WebShopApi::class.java)
    }

    override fun getAllDevices(): Single<List<Product>> = webShopApi.getAllDevices()

    override fun getDevice(id: Long):  Single<Product>  = webShopApi.getDevice(id)

    override fun getAllPromotionalDevices():  Single<List<Product>> = webShopApi.getAllPromotionalDevices()
}