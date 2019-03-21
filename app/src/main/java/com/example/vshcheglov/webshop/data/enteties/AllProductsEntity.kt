package com.example.vshcheglov.webshop.data.enteties

import com.example.vshcheglov.webshop.domain.Product

data class AllProductsEntity(var products: MutableList<Product>, var promotionalProducts: MutableList<Product>)