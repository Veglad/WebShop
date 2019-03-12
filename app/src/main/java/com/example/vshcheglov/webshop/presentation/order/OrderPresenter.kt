package com.example.vshcheglov.webshop.presentation.order

import com.example.vshcheglov.webshop.domain.Basket

class OrderPresenter(private val orderView: IOrderView) {

    fun initOrderPrice() {
        val orderPrice = Basket.totalPriceWithDiscount
        orderView.setOrderPrice(orderPrice)
    }

    interface IOrderView {
        fun setOrderPrice(orderPrice: Double)
    }
}