package com.example.vshcheglov.webshop.presentation.entites

data class BasketPresentation(
    var cardsNumber: Int = 0,
    var productsNumber: Int = 0,
    var totalPriceDiscount: String = "0.0",
    var listOfProductLists: List<List<ProductPresentation>> = mutableListOf()
)