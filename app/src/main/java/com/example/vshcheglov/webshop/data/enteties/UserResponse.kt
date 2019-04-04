package com.example.vshcheglov.webshop.data.enteties

data class UserResponse (var email: String?, var id: String) {

    constructor() : this("", "") //TODO: Investigate serialization
}