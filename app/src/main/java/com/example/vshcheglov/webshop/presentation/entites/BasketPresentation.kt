package com.example.vshcheglov.webshop.presentation.entites

data class BasketPresentation(
    val cardsNumber: Int = 0,
    val productsNumber: Int = 0,
    val totalPriceDiscount: String = "0.0",
    val listOfProductLists: List<List<ProductPresentation>> = mutableListOf()
)