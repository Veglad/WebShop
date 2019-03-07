package com.example.vshcheglov.webshop.presentation.entites

data class BasketPresentation(
    var cardsNumber: Int = 0,
    var productsNumber: Int = 0,
    var totalPriceDiscount: Double = 0.0,
    var listOfProductLists: MutableList<Pair<ProductPresentation, Int>> = mutableListOf()
)