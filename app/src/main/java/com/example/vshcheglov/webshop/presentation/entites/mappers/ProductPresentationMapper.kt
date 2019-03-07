package com.example.vshcheglov.webshop.presentation.entites.mappers

import android.content.Context
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.entites.ProductPresentation

object ProductPresentationMapper {

    fun transform(product: Product) = ProductPresentation().apply {
        id = product.id
        name = product.name
        price = product.price
        imageThumbnailUrl = product.imageThumbnailUrl
        shortDescription = product.shortDescription
        longDescription = product.longDescription
        imageUrl = product.imageUrl
        inStockNumber = product.inStockNumber
        purchasesNumber = product.purchasesNumber
        percentageDiscount = product.percentageDiscount
        priceWithDiscount = product.priceWithDiscount
    }

    fun transform(productCollection: Collection<Product>) = mutableListOf<ProductPresentation>().apply {
        for (product in productCollection) {
            this.add(transform(product))
        }
    }
}