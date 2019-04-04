package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.OrderResponseProduct
import com.example.vshcheglov.webshop.data.enteties.RealmOrderProduct
import com.example.vshcheglov.webshop.domain.common.Mapper

class RealmResponseOrderProductMapper : Mapper<RealmOrderProduct, OrderResponseProduct> {
    override fun map(from: RealmOrderProduct) = OrderResponseProduct(
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