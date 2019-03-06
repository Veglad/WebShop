package com.example.vshcheglov.webshop.presentation.order

import android.content.Context

interface IOrderView {
    fun showOrderCompleted(orderMessage: String)

    fun setOrderPrice(orderPrice: String)

    val context: Context
}