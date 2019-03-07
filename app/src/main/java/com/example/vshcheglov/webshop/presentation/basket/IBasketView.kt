package com.example.vshcheglov.webshop.presentation.basket

import android.content.Context
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product

interface IBasketView {
    fun startOrderActivity()

    fun setBasketAmount(amount: String)

    fun setBasketItemsNumber(itemsNumber: String)

    fun showUndo(undoTitle: String)

    fun showBasket(basket: Basket)

    fun setOrderButtonIsEnabled(isEnabled: Boolean)

    fun removeProductFromList(position: Int)

    fun restoreProduct(mapPair: Pair<Int, MutableList<Product>>, deletedIndex: Int)

    fun setSameProductsNumber(position: Int, number: Int)

    fun setTotalProductPrice(position: Int, totalDiscountPrice: Double)

    fun setTotalProductPriceTitle(position: Int, totalPrice: Double)

    val context: Context
}