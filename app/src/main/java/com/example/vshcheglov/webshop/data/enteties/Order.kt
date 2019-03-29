package com.example.vshcheglov.webshop.data.enteties

import com.example.vshcheglov.webshop.domain.Product
import com.google.firebase.Timestamp

data class Order(
    var productToNumberList: MutableList<OrderProduct>,
    var timestamp: Timestamp,
    var amount: Double,
    var id: String = "") {

    constructor() : this(mutableListOf(), Timestamp.now(), 0.0)

}

data class OrderProduct(
    var id: Int = -1,
    var name: String = "Noname",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var count: Int = 0)