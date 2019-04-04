package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.OrderResponse
import com.example.vshcheglov.webshop.data.enteties.OrderResponseProduct
import com.example.vshcheglov.webshop.data.enteties.OrderRealmProduct
import com.example.vshcheglov.webshop.data.enteties.RealmOrder
import com.example.vshcheglov.webshop.domain.common.Mapper
import com.google.firebase.Timestamp
import io.realm.RealmList

class RealmOrderOrderNetworkMapper : Mapper<RealmOrder, OrderResponse> {
    override fun map(from: RealmOrder): OrderResponse {
        val networkProductList = mutableListOf<OrderResponseProduct>().apply {
            for (realmProduct in from.orderProducts) {
                add(map(realmProduct))
            }
        }

        return OrderResponse(networkProductList, Timestamp(from.timestampDate), from.amount, from.id)
    }

    fun map(from: OrderResponse): RealmOrder {
        val realmProductList = RealmList<OrderRealmProduct>().apply {
            for (networkProduct in from.orderProducts) {
                add(map(networkProduct))
            }
        }

        return RealmOrder(realmProductList, from.timestamp.toDate(), from.amount, from.id)
    }

    fun map(from: OrderRealmProduct) = OrderResponseProduct(
        from.id,
        from.productId,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )

    fun map(from: OrderResponseProduct) = OrderRealmProduct(
        from.id,
        from.productId,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )
}