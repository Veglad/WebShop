package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.OrderNetwork
import com.example.vshcheglov.webshop.data.enteties.OrderNetworkProduct
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.domain.OrderProduct
import com.example.vshcheglov.webshop.domain.common.Mapper

class OrderNetworkOrderMapper : Mapper<OrderNetwork, Order> {
    override fun map(from: OrderNetwork): Order {
        val orderList = mutableListOf<OrderProduct>().apply {
            for (networkProduct in from.orderProducts) {
                add(map(networkProduct))
            }
        }

        return Order(orderList, from.timestamp, from.amount, from.id)
    }

    fun map(from: Order): OrderNetwork {
        val orderNetworkList = mutableListOf<OrderNetworkProduct>().apply {
            for (product in from.orderProducts) {
                add(map(product))
            }
        }

        return OrderNetwork(orderNetworkList, from.timestamp, from.amount, from.id)
    }

    fun map(from: OrderNetworkProduct) = OrderProduct(
        from.id,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )

    fun map(from: OrderProduct) = OrderNetworkProduct(
        from.id,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )
}