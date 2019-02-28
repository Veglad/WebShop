package com.example.vshcheglov.webshop.domain

object Basket {
    var productList = mutableListOf<Product>()
        private set
    
    var size = 0
        get() = productList.size
        private set

    var totalPrice = 0.0
        get() {
            return if (productList.isEmpty()) {
                0.0
            } else {
                productList.sumByDouble { it.price }
            }
        }
        private set
}