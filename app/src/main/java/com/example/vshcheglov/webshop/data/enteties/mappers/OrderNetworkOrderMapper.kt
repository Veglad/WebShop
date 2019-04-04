package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.OrderResponse
import com.example.vshcheglov.webshop.data.enteties.OrderResponseProduct
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.domain.OrderProduct
import com.example.vshcheglov.webshop.domain.common.Mapper

class OrderNetworkOrderMapper : Mapper<OrderResponse, Order> {
    override fun map(from: OrderResponse): Order {
        val orderList = mutableListOf<OrderProduct>().apply {
            for (networkProduct in from.orderProducts) {
                add(map(networkProduct))
            }
        }

        return Order(orderList, from.timestamp, from.amount, from.id)
    }

    fun map(from: Order): OrderResponse {
        val orderNetworkList = mutableListOf<OrderResponseProduct>().apply {
            for (product in from.orderProducts) {
                add(map(product))
            }
        }

        return OrderResponse(orderNetworkList, from.timestamp, from.amount, from.id)
    }

    fun map(from: OrderResponseProduct) = OrderProduct(
        from.id,
        from.productId,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )

    fun map(from: OrderProduct) = OrderResponseProduct(
        from.id,
        from.productId,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )
}