package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.OrderNetwork
import com.example.vshcheglov.webshop.data.enteties.OrderNetworkProduct
import com.example.vshcheglov.webshop.data.enteties.OrderRealmProduct
import com.example.vshcheglov.webshop.data.enteties.RealmOrder
import com.example.vshcheglov.webshop.domain.common.Mapper
import com.google.firebase.Timestamp
import io.realm.RealmList

class RealmOrderOrderNetworkMapper : Mapper<RealmOrder, OrderNetwork> {
    override fun map(from: RealmOrder): OrderNetwork {
        val networkProductList = mutableListOf<OrderNetworkProduct>().apply {
            for (realmProduct in from.orderProducts) {
                add(map(realmProduct))
            }
        }

        return OrderNetwork(networkProductList, Timestamp(from.timestampDate), from.amount, from.id)
    }

    fun map(from: OrderNetwork): RealmOrder {
        val realmProductList = RealmList<OrderRealmProduct>().apply {
            for (networkProduct in from.orderProducts) {
                add(map(networkProduct))
            }
        }

        return RealmOrder(realmProductList, from.timestamp.toDate(), from.amount, from.id)
    }

    fun map(from: OrderRealmProduct) = OrderNetworkProduct(
        from.id,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )

    fun map(from: OrderNetworkProduct) = OrderRealmProduct(
        from.id,
        from.name,
        from.price,
        from.imageUrl,
        from.count
    )
}