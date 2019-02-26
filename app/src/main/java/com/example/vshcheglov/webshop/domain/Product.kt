package com.example.vshcheglov.webshop.domain

data class Product(
    var id: Int = -1,
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var imageUrl: String = ""
)