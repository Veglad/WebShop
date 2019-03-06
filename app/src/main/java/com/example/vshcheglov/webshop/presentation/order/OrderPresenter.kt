package com.example.vshcheglov.webshop.presentation.order

import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Basket

class OrderPresenter(private val orderView: IOrderView) {

    fun buttonOrderClick() {
        orderView.showOrderCompleted(orderView.context.getString(R.string.order_completed))
    }

    fun onCreate() {
        val orderPrice =
            String.format(orderView.context.getString(R.string.price_format), Basket.totalPriceWithDiscount)
        orderView.setOrderPrice(orderPrice)
    }

}