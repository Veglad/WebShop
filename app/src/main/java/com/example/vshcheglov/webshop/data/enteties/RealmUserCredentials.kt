package com.example.vshcheglov.webshop.data.enteties

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmUserCredentials(
    @PrimaryKey
    var id: Int = 1,
    var email: String = "",
    var encryptedPassword: String = "") : RealmObject()