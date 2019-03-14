package com.example.vshcheglov.webshop.presentation.order

import com.example.vshcheglov.webshop.domain.Basket

class OrderPresenter(private var orderView: OrderView?) {

    fun initOrderPrice() {
        val orderPrice = Basket.totalPriceWithDiscount
        orderView?.setOrderPrice(orderPrice)
    }

    fun onAttached(orderView: OrderView) {
        this.orderView = orderView
    }

    fun onDetached() {
        orderView = null
    }

    interface OrderView {
        fun setOrderPrice(orderPrice: Double)
    }
}