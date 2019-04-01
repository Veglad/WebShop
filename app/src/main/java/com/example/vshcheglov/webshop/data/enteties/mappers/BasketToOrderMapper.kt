package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.OrderNetwork
import com.example.vshcheglov.webshop.data.enteties.OrderProductNetwork
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.domain.common.Mapper
import com.google.firebase.Timestamp

class BasketToOrderMapper: Mapper<Basket, OrderNetwork> {
    override fun map(from: Basket) = OrderNetwork(
        map(from.productToCountList),
        Timestamp.now(),
        Basket.totalPriceWithDiscount)

    fun map(from: MutableList<Pair<Product, Int>>) = mutableListOf<OrderProductNetwork>().also {
        for (productToCount in from) {
            val orderProductToCount = map(productToCount.first)
            orderProductToCount.count = productToCount.second
            it.add(orderProductToCount)
        }
    }

    fun map(from: Product) = OrderProductNetwork(
        from.id,
        from.name,
        from.priceWithDiscount,
        from.imageThumbnailUrl)
}