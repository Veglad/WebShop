package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.Order
import com.example.vshcheglov.webshop.data.enteties.OrderProduct
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.domain.common.Mapper
import com.google.firebase.Timestamp

class BasketToOrderMapper: Mapper<Basket, Order> {
    override fun map(from: Basket) = Order(
        map(from.productToCountList),
        Timestamp.now(),
        Basket.totalPriceWithDiscount)

    fun map(from: MutableList<Pair<Product, Int>>) = mutableListOf<Pair<OrderProduct, Int>>().also {
        for (productToCount in from) {
            val orderProductToCount = Pair(map(productToCount.first), productToCount.second)
            it.add(orderProductToCount)
        }
    }

    fun map(from: Product) = OrderProduct(
        from.id,
        from.name,
        from.priceWithDiscount,
        from.imageThumbnailUrl)
}