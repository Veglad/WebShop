package com.example.vshcheglov.webshop.domain

object Basket {
    private var productList = mutableListOf<Product>()
    
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

    fun clear() {
        productList.clear()
        totalPrice = 0.0
    }

    fun addProduct(product: Product) {
        productList.add(product)
    }

    fun getProduct(index: Int) = if (isValidIndex(index)) {
        productList[index]
    } else {
        null
    }

    fun removeProduct(product: Product) = productList.remove(product)

    fun removeProductByPIndex(index: Int): Boolean {
        if (isValidIndex(index)) {
            productList.removeAt(index)
            return true
        }
        return false
    }

    private fun isValidIndex(index: Int) = index >= 0 && index < productList.size
}