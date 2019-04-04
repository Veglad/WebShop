package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.ProductResponse
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.domain.common.Mapper

class ResposeProductMapper : Mapper<ProductResponse, Product> {

    override fun map(from: ProductResponse) = Product().apply {
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

    fun map(from: Collection<ProductResponse>) = mutableListOf<Product>().apply {
        for (productEntity in from) {
            this.add(map(productEntity))
        }
    }
}