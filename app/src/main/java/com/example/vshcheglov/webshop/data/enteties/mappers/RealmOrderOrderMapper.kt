package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.OrderRealmProduct
import com.example.vshcheglov.webshop.data.enteties.RealmOrder
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.domain.OrderProduct
import com.example.vshcheglov.webshop.domain.common.Mapper
import com.google.firebase.Timestamp
import io.realm.RealmList

class RealmOrderOrderMapper : Mapper<RealmOrder, Order> {
    override fun map(from: RealmOrder): Order {
        val productList = mutableListOf<OrderProduct>().apply {
            for (realmProduct in from.orderProducts) {
                add(map(realmProduct))
            }
        }

        return Order(productList, Timestamp(from.timestampDate), from.amount, from.id)
    }

    fun map(from: Order): RealmOrder {
        val realmProductList = RealmList<OrderRealmProduct>().apply {
            for (product in from.orderProducts) {
                add(map(product))
            }
        }

        return RealmOrder(realmProductList, from.timestamp.toDate(), from.amount, from.id)
    }

    fun map(from: OrderRealmProduct) = OrderProduct(
        from.id,
        from.productId,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )

    fun map(from: OrderProduct) = OrderRealmProduct(
        from.id,
        from.productId,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )
}