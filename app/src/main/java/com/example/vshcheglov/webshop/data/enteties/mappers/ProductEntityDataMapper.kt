package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.ProductEntity
import com.example.vshcheglov.webshop.domain.Product

object ProductEntityDataMapper {

    fun transform(productEntity: ProductEntity) = Product().apply {
        id = productEntity.deviceId
        name = productEntity.name
        price = productEntity.price
        imageThumbnailUrl = productEntity.imageThumbnailUrl
        shortDescription = productEntity.shortDescription
        longDescription = productEntity.longDescription
        imageUrl = productEntity.imageUrl
        inStockNumber = productEntity.inStok
        purchasesNumber = productEntity.bought
        percentageDiscount = productEntity.promotional
    }

    fun transform(productEntityCollection: Collection<ProductEntity>) = mutableListOf<Product>().apply {
        for (productEntity in productEntityCollection) {
            this.add(transform(productEntity))
        }
    }
}