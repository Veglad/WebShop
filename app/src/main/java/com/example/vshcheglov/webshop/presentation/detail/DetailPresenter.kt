package com.example.vshcheglov.webshop.presentation.detail

import android.os.Bundle
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product

class DetailPresenter(private val detailView: IDetailView) {

    private lateinit var product: Product

    fun onCreate(extras: Bundle?) {
        product = extras?.getParcelable(DetailActivity.PRODUCT_KEY) ?: Product()
        detailView.showProductInfo(product)
    }

    fun buyProductClick() {
        Basket.addProduct(product)
        detailView.startBasketActivity()
    }
}