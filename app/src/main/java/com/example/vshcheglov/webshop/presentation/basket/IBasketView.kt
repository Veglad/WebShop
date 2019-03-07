package com.example.vshcheglov.webshop.presentation.basket

import android.content.Context
import com.example.vshcheglov.webshop.presentation.entites.BasketPresentation
import com.example.vshcheglov.webshop.presentation.entites.ProductPresentation

interface IBasketView {
    fun startOrderActivity()

    fun setBasketAmount(amount: String)

    fun setBasketItemsNumber(itemsNumber: String)

    fun showUndo(undoTitle: String)

    fun showBasket(basket: BasketPresentation)

    fun setOrderButtonIsEnabled(isEnabled: Boolean)

    fun removeSameProductsCard(position: Int)

    fun restoreSameProductsCard(productToNumberPair: Pair<ProductPresentation, Int>, deletedIndex: Int)

    fun setSameProductsNumber(position: Int, number: Int)

    fun setTotalProductPrice(position: Int, totalDiscountPrice: Double)

    fun setTotalProductPriceTitle(position: Int, totalPrice: Double)

    val context: Context
}