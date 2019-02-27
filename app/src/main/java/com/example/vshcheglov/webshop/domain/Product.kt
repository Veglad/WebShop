package com.example.vshcheglov.webshop.domain

class Product {
    var id = -1
    var name = "Noname"
    var shortDescription = "no"
    var longDescription = "no"
    var price = 0.0
    var imageThumbnailUrl = ""
    var imageUrl = ""
    var inStok = 0
    var isPopular = true
    var bought = 0
    var categoryId = 0
    var promotional = 0
    var addDate = ""

    fun initProduct(
        id: Int, name: String, shortDescription: String, longDescription: String,
        price: Double, imageThumbnailUrl: String, imageUrl: String, inStok: Int,
        isPopular: Boolean, bought: Int, categoryId: Int, promotional: Int, addDate: String
    ) {
        this.id = id
        this.name = name
        this.shortDescription = shortDescription
        this.longDescription = longDescription
        this.price = price
        this.imageThumbnailUrl = imageThumbnailUrl
        this.imageUrl = imageUrl
        this.inStok = inStok
        this.isPopular = isPopular
        this.bought = bought
        this.categoryId = categoryId
        this.promotional = promotional
        this.addDate = addDate
    }
}