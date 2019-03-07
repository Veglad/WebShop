package com.example.vshcheglov.webshop.presentation.entites.mappers

import android.content.Context
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.entites.BasketPresentation

object BasketPresentationMapper {

    fun transform(basket: Basket) = BasketPresentation().apply {
        cardsNumber = basket.mapSize
        productsNumber = basket.listOfProduct.size
        totalPriceDiscount = basket.totalPriceWithDiscount
        listOfProductLists = getListFromMap(basket.productListMap)
    }

    private fun getListFromMap(productListMap: LinkedHashMap<Int, MutableList<Product>>) =
        productListMap.values.map {
            Pair(ProductPresentationMapper.transform(it[0]), it.size)
        }.toMutableList()
}