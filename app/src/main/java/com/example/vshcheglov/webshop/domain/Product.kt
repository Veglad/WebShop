package com.example.vshcheglov.webshop.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    var deviceId: Int = -1,
    var name: String = "Noname",
    var price: Double = 0.0,
    var imageThumbnailUrl: String = "",
    var shortDescription: String = "no"
) : Parcelable {
    var longDescription = "no"
    var imageUrl = ""
    var inStok = 0
    var isPopular = true
    var bought = 0
    var categoryId = 0
    var promotional = 0
    var addDate = ""
}