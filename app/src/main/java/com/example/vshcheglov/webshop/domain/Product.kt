package com.example.vshcheglov.webshop.domain

data class Product(val id: Int,
              val name: String,
              val shortDescription: String,
              val price: Double,
              val imageUrl: String)