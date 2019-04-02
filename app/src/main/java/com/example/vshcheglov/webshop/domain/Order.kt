package com.example.vshcheglov.webshop.domain

import com.google.firebase.Timestamp

data class Order(
    var orderProducts: MutableList<OrderProduct>,
    var timestamp: Timestamp,
    var amount: Double,
    var id: String = "")

data class OrderProduct(
    var id: Int = -1,
    var name: String = "Noname",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var count: Int = 0)