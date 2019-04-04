package com.example.vshcheglov.webshop.data.enteties

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class RealmOrder(
    var orderProducts: RealmList<OrderRealmProduct>,
    var timestampDate: Date,
    var amount: Double,
    @PrimaryKey
    var id: String = ""
) : RealmObject() {
    constructor() : this(RealmList<OrderRealmProduct>(), Date(), 0.0)
}


open class OrderRealmProduct(
    @PrimaryKey
    var id: String = "",
    var productId: Int = 0,
    var name: String = "Noname",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var count: Int = 0) : RealmObject()

