package com.example.vshcheglov.webshop.presentation.entites.mappers

import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.entites.ProductBasketCard

object ProductBasketCardMapper {

    fun transform(productList: List<Product>, totalProductPrice: Double,
                  totalProductPriceDiscount: Double) = ProductBasketCard().also {
        val product = productList[0]
        it.name = product.name
        it.description = product.shortDescription
        it.imageUrl = product.imageThumbnailUrl
        it.productsNumber = productList.size
        it.totalPriceDiscount = totalProductPriceDiscount
        it.totalPrice = totalProductPrice
        it.productPriceDiscount = product.priceWithDiscount
        it.productPrice = product.price
        it.percentageDiscount = product.percentageDiscount.toDouble()
    }

    fun transform(basket: Basket) = mutableListOf<ProductBasketCard>().apply {
        for (idToProductList in basket.productListMap) {
            val id = idToProductList.key
            val totalProductPrice = basket.getTotalProductPrice(id)
            val totalProductPriceDiscount = basket.getTotalDiscountProductPrice(id)
            this.add(transform(idToProductList.value, totalProductPrice, totalProductPriceDiscount))
        }
    }
}