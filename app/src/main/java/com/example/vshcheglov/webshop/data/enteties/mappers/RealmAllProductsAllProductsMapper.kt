package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.RealmAllProducts
import com.example.vshcheglov.webshop.data.enteties.RealmProduct
import com.example.vshcheglov.webshop.domain.AllProducts
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.domain.common.Mapper

class RealmAllProductsAllProductsMapper(val mapper: RealmProductProductMapper)
    : Mapper<RealmAllProducts, AllProducts> {

    override fun map(from: RealmAllProducts) = AllProducts(
        mapRealmProducts(from.products),
        mapRealmProducts(from.promotionalProducts)
    )

    fun map(from: AllProducts) = RealmAllProducts(
        mapProducts(from.products),
        mapProducts(from.promotionalProducts)
    )

    fun mapProducts(from: List<Product>) = mutableListOf<RealmProduct>().apply {
        for (product in from) {
            add(mapper.map(product))
        }
    }

    fun mapRealmProducts(from: List<RealmProduct>) = mutableListOf<Product>().apply {
        for (product in from) {
            add(mapper.map(product))
        }
    }
}