package com.example.vshcheglov.webshop.data.enteties

import com.google.firebase.Timestamp
import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

open class RealmOrder(
    var orderProducts: RealmList<OrderRealmProduct>,
    var timestampDate: Date,
    var amount: Double,
    var id: String = ""
) : RealmObject() {
    constructor() : this(RealmList<OrderRealmProduct>(), Date(), 0.0)
}


open class OrderRealmProduct(
    var id: Int = -1,
    var name: String = "Noname",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var count: Int = 0) : RealmObject()

