package com.example.vshcheglov.webshop.presentation.detail

import android.os.Bundle
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product

class DetailPresenter(private val detailView: IDetailView) {

    private lateinit var product: Product

    fun showProductInfo(product: Product?) {
        this.product = product?: Product()
        detailView.showProductInfo(this.product)
    }

    fun buyProduct() {
        Basket.addProduct(product)
        detailView.startBasketActivity()
    }

    interface IDetailView {
        fun startBasketActivity()

        fun showProductInfo(product: Product)
    }
}