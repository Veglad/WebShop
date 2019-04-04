package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.OrderResponse
import com.example.vshcheglov.webshop.data.enteties.OrderResponseProduct
import com.example.vshcheglov.webshop.data.enteties.RealmOrderProduct
import com.example.vshcheglov.webshop.data.enteties.RealmOrder
import com.example.vshcheglov.webshop.domain.common.Mapper
import com.google.firebase.Timestamp
import io.realm.RealmList

class RealmResponseOrderMapper : Mapper<RealmOrder, OrderResponse> {
    override fun map(from: RealmOrder): OrderResponse {
        val networkProductList = mutableListOf<OrderResponseProduct>().apply {
            for (realmProduct in from.orderProducts) {
                add(map(realmProduct))
            }
        }

        return OrderResponse(networkProductList, Timestamp(from.timestampDate), from.amount, from.id)
    }

    fun map(to: OrderResponse): RealmOrder {
        val realmProductList = RealmList<RealmOrderProduct>().apply {
            for (networkProduct in to.orderProducts) {
                add(map(networkProduct))
            }
        }

        return RealmOrder(realmProductList, to.timestamp.toDate(), to.amount, to.id)
    }

    fun map(from: RealmOrderProduct) = OrderResponseProduct(
        from.id,
        from.productId,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )

    fun map(to: OrderResponseProduct) = RealmOrderProduct(
        to.id,
        to.productId,
        to.name,
        to.price,
        to.imageUrl,
        to.count
    )
}