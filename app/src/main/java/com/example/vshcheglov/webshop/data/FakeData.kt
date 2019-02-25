package com.example.vshcheglov.webshop.data

import android.content.Context
import com.beust.klaxon.Klaxon
import com.example.vshcheglov.webshop.domain.Product

fun getFakeProducts(context: Context) =
    Klaxon().parseArray<Product>(context.assets.open("products.json"))