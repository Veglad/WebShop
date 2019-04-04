package com.example.vshcheglov.webshop.data.enteties

import com.google.firebase.Timestamp
import io.realm.RealmObject

data class OrderNetwork(
    var orderProducts: MutableList<OrderNetworkProduct>,
    var timestamp: Timestamp,
    var amount: Double,
    var id: String = ""
) {
    constructor() : this(mutableListOf(), Timestamp.now(), 0.0)
}


data class OrderNetworkProduct(
    var id: String = "",
    var productId: Int = 0,
    var name: String = "Noname",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var count: Int = 0)

