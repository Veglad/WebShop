package com.example.vshcheglov.webshop.domain

import com.google.firebase.Timestamp

data class OrderNetwork(
    var orderProducts: MutableList<OrderProductNetwork>,
    var timestamp: Timestamp,
    var amount: Double,
    var id: String = "") {

    constructor() : this(mutableListOf(), Timestamp.now(), 0.0)

}

data class OrderProductNetwork(
    var id: Int = -1,
    var name: String = "Noname",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var count: Int = 0)