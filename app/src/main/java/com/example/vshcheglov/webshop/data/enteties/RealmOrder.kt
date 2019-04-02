package com.example.vshcheglov.webshop.data.enteties

import com.google.firebase.Timestamp

open class RealmOrder(
    var orderProducts: MutableList<RealmProduct>,
    var timestamp: Timestamp,
    var amount: Double,
    var id: String = ""
)