package com.example.vshcheglov.webshop.data.enteties

data class UserNetwork (var email: String?, var id: String) {

    constructor() : this("", "") //TODO: Investigate serialization
}