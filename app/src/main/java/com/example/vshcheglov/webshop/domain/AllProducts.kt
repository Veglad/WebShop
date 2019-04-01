package com.example.vshcheglov.webshop.domain

import com.example.vshcheglov.webshop.domain.Product

data class AllProducts(var products: MutableList<Product>, var promotionalProducts: MutableList<Product>)