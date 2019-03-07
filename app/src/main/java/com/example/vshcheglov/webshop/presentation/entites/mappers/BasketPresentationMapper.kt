package com.example.vshcheglov.webshop.presentation.entites.mappers

import android.content.Context
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.entites.BasketPresentation
import com.example.vshcheglov.webshop.presentation.entites.ProductPresentation

object BasketPresentationMapper {

    fun transform(basket: Basket, context: Context) = BasketPresentation().apply {
        cardsNumber = basket.mapSize
        productsNumber = basket.listOfProduct.size
        totalPriceDiscount = String.format(context.getString(R.string.price_format), basket.totalPriceWithDiscount)
        listOfProductLists = getListFromMap(basket.productListMap)
    }

    private fun getListFromMap(productListMap: LinkedHashMap<Int, MutableList<Product>>) =
        productListMap.values.map { ProductPresentationMapper.transform(it)}
}