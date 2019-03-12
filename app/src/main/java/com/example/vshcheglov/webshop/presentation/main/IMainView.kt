package com.example.vshcheglov.webshop.presentation.main

import android.content.Context
import com.example.vshcheglov.webshop.domain.Product

interface IMainView {
    fun showLoading()

    fun hideLoading()

    fun setShowRetry(isVisible: Boolean)

    fun showError(errorMessage: String)

    fun showProductList(productList: List<Product>)

    fun showPromotionalProductList(promotionalList: List<Product>)

    val context: Context
}