package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.OrderResponse
import com.example.vshcheglov.webshop.data.enteties.OrderResponseProduct
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.domain.OrderProduct
import com.example.vshcheglov.webshop.domain.common.Mapper

class ResponseOrderProductMapper : Mapper<OrderResponseProduct, OrderProduct> {
    override fun map(from: OrderResponseProduct) = OrderProduct(
        from.id,
        from.productId,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )

    fun map(to: OrderProduct) = OrderResponseProduct(
        to.id,
        to.productId,
        to.name,
        to.price,
        to.imageUrl,
        to.count
    )
}