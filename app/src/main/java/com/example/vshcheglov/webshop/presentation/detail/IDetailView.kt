package com.example.vshcheglov.webshop.presentation.detail

import com.example.vshcheglov.webshop.domain.Product

interface IDetailView {
    fun startBasketActivity()

    fun showProductInfo(product: Product)
}