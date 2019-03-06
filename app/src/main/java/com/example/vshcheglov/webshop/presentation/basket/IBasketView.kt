package com.example.vshcheglov.webshop.presentation.basket

import android.content.Context
import com.example.vshcheglov.webshop.domain.Product

interface IBasketView {
    fun startOrderActivity()

    fun setBasketAmount(amount: String)

    fun setBasketItemsNumber(itemsNumber: String)

    fun showUndo(undoTitle: String)

    fun setOrderButtonIsEnabled(isEnabled: Boolean)

    fun removeProductFromList(position: Int)

    fun restoreProduct(mapPair: Pair<Int, MutableList<Product>>, deletedIndex: Int)

    val context: Context
}