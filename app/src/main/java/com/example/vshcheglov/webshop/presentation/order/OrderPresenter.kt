package com.example.vshcheglov.webshop.presentation.order

import com.example.vshcheglov.webshop.domain.Basket

class OrderPresenter(private val orderView: OrderView) {

    fun initOrderPrice() {
        val orderPrice = Basket.totalPriceWithDiscount
        orderView.setOrderPrice(orderPrice)
    }

    interface OrderView {
        fun setOrderPrice(orderPrice: Double)
    }
}