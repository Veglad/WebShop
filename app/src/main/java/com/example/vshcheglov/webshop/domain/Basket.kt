package com.example.vshcheglov.webshop.domain

object Basket {

    var productListMap = linkedMapOf<Int, MutableList<Product>>()
        private set

    val listOfProduct: List<Product>
        get() {
            return productListMap.flatMap { entry -> entry.value}
        }
    var mapSize = 0
        get() = productListMap.size
        private set

    var productListSize = 0
        get() = listOfProduct.size
        private set

    var totalPrice = 0.0
        get() {
            return if (productListMap.isEmpty()) {
                0.0
            } else {
                productListMap.flatMap { entry -> entry.value }.sumByDouble { it.getPriceWithDiscount() }
            }
        }
        private set

    fun addProduct(product: Product) {
        if(productListMap.containsKey(product.deviceId)) {
            productListMap[product.deviceId]?.add(product)
        } else {
            productListMap[product.deviceId] = mutableListOf(product)
        }
    }

    fun removeProductIfAble(product: Product) {
        val productList = productListMap[product.deviceId]
        if(productList != null && productList.size > 1) {
            productList.remove(product)
        }
    }

    fun removeSameProducts(productId: Int) = productListMap.remove(productId)
}