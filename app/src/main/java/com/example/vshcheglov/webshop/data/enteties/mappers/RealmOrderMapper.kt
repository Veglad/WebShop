package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.RealmOrderProduct
import com.example.vshcheglov.webshop.data.enteties.RealmOrder
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.domain.OrderProduct
import com.example.vshcheglov.webshop.domain.common.Mapper
import com.google.firebase.Timestamp
import io.realm.RealmList

class RealmOrderMapper : Mapper<RealmOrder, Order> {
    override fun map(from: RealmOrder): Order {
        val productList = mutableListOf<OrderProduct>().apply {
            for (realmProduct in from.orderProducts) {
                add(map(realmProduct))
            }
        }

        return Order(productList, Timestamp(from.timestampDate), from.amount, from.id)
    }

    fun map(to: Order): RealmOrder {
        val realmProductList = RealmList<RealmOrderProduct>().apply {
            for (product in to.orderProducts) {
                add(map(product))
            }
        }

        return RealmOrder(realmProductList, to.timestamp.toDate(), to.amount, to.id)
    }

    fun map(from: RealmOrderProduct) = OrderProduct(
        from.id,
        from.productId,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )

    fun map(to: OrderProduct) = RealmOrderProduct(
        to.id,
        to.productId,
        to.name,
        to.price,
        to.imageUrl,
        to.count
    )
}