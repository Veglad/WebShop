package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.RealmProduct
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.domain.common.Mapper

class RealmProductProductMapper : Mapper<RealmProduct, Product> {

    override fun map(from: RealmProduct) = Product(
            from.id,
            from.name,
            from.price,
            from.imageThumbnailUrl,
            from.shortDescription,
            from.longDescription,
            from.imageUrl,
            from.inStockNumber,
            from.purchasesNumber,
            from.percentageDiscount)

    fun map(from: Product) = RealmProduct(
            from.id,
            from.name,
            from.price,
            from.imageThumbnailUrl,
            from.shortDescription,
            from.longDescription,
            from.imageUrl,
            from.inStockNumber,
            from.purchasesNumber,
            from.percentageDiscount)
}