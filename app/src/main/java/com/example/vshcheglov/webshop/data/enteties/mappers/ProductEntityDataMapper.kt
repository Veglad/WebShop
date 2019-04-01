package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.ProductEntity
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.domain.common.Mapper

class ProductEntityDataMapper : Mapper<ProductEntity, Product> {

    override fun map(from: ProductEntity) = Product().apply {
        id = from.deviceId
        name = from.name
        price = from.price
        imageThumbnailUrl = from.imageThumbnailUrl
        shortDescription = from.shortDescription
        longDescription = from.longDescription
        imageUrl = from.imageUrl
        inStockNumber = from.inStok
        purchasesNumber = from.bought
        percentageDiscount = from.promotional
    }

    fun map(from: Collection<ProductEntity>) = mutableListOf<Product>().apply {
        for (productEntity in from) {
            this.add(map(productEntity))
        }
    }
}