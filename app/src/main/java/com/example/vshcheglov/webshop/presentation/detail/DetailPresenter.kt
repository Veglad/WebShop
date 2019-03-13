package com.example.vshcheglov.webshop.presentation.detail

import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product

class DetailPresenter(private var detailView: DetailView?) {

    private lateinit var product: Product

    fun showProductInfo(product: Product?) {
        this.product = product ?: Product()
        detailView?.showProductInfo(this.product)
    }

    fun buyProduct() {
        Basket.addProduct(product)
        detailView?.startBasketActivity()
    }

    fun onAttached(detailView: DetailView) {
        this.detailView = detailView
    }

    fun onDetached() {
        detailView = null
    }

    interface DetailView {
        fun startBasketActivity()

        fun showProductInfo(product: Product)
    }
}