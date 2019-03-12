package com.example.vshcheglov.webshop.presentation.entites.mappers

import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.entites.ProductBasketCard

object ProductBasketCardMapper {

    fun transform(product: Product, productCount: Int, totalProductPrice: Double,
                  totalProductPriceDiscount: Double) = ProductBasketCard().also {
        it.name = product.name
        it.description = product.shortDescription
        it.imageUrl = product.imageThumbnailUrl
        it.productsNumber = productCount
        it.totalPriceDiscount = totalProductPriceDiscount
        it.totalPrice = totalProductPrice
        it.productPriceDiscount = product.priceWithDiscount
        it.productPrice = product.price
        it.percentageDiscount = product.percentageDiscount.toDouble()
    }

    fun transform(basket: Basket) = mutableListOf<ProductBasketCard>().apply {
        for (productToCount in basket.productToCountList) {
            val product = productToCount.first
            val productCount = productToCount.second
            val totalProductPrice = basket.getSameProductPrice(product.id)
            val totalProductPriceDiscount = basket.getSameProductDiscountPrice(product.id)
            this.add(transform(product, productCount, totalProductPrice, totalProductPriceDiscount))
        }
    }
}