package com.example.vshcheglov.webshop.presentation.order

import com.example.vshcheglov.webshop.domain.Basket
import nucleus.presenter.Presenter

class OrderPresenter : Presenter<OrderPresenter.OrderView>() {

    fun initOrderPrice() {
        val orderPrice = Basket.totalPriceWithDiscount
        view?.setOrderPrice(orderPrice)

    }

    interface OrderView {
        fun setOrderPrice(orderPrice: Double)
    }
}