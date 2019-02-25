package com.example.vshcheglov.webshop.domain

data class Product(val id: Int,
                   val name: String,
                   val description: String,
                   val price: Double,
                   val imageUrl: String)