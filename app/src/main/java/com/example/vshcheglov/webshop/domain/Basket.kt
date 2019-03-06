package com.example.vshcheglov.webshop.domain

object Basket {

    var productListMap = linkedMapOf<Int, MutableList<Product>>()
        private set

    val listOfProduct: List<Product>
        get() {
            return productListMap.flatMap { entry -> entry.value }
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
                productListMap.flatMap { entry -> entry.value }
                    .sumByDouble { it.price }
            }
        }
        private set

    var totalPriceWithDiscount = 0.0
        get() {
            return if (productListMap.isEmpty()) {
                0.0
            } else {
                productListMap.flatMap { entry -> entry.value }
                    .sumByDouble { it.priceWithDiscount }
            }
        }
        private set

    fun addProduct(product: Product) {
        if (productListMap.containsKey(product.id)) {
            productListMap[product.id]?.add(product)
        } else {
            productListMap[product.id] = mutableListOf(product)
        }
    }

    fun addPair(pairProduct: Pair<Int, MutableList<Product>>, position: Int) {
        val updatedListOfPairs = productListMap.toList()
            .toMutableList()
        updatedListOfPairs.add(position, pairProduct)
        productListMap = LinkedHashMap(updatedListOfPairs.toMap())
    }

    fun removeProductIfAble(product: Product) {
        val productList = productListMap[product.id]
        if (productList != null && productList.size > 1) {
            productList.remove(product)
        }
    }

    fun getTotalProductPrice(productId: Int): Double {
        val productList = productListMap[productId]
        return productList?.sumByDouble { it.price } ?: 0.0
    }

    fun getTotalDiscountProductPrice(productId: Int): Double {
        val productList = productListMap[productId]
        return productList?.sumByDouble { it.priceWithDiscount } ?: 0.0
    }

    fun removeSameProducts(index: Int) {
        val updatedListOfPairs = productListMap.toList()
            .toMutableList()
        updatedListOfPairs.removeAt(index)
        productListMap = LinkedHashMap(updatedListOfPairs.toMap())
    }
}