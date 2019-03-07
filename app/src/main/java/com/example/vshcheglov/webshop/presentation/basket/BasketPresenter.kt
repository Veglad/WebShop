package com.example.vshcheglov.webshop.presentation.basket

import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import kotlin.properties.Delegates

class BasketPresenter(private val basketView: IBasketView) {

    private var totalPriceWithDiscount = Basket.totalPriceWithDiscount
    private var productListSize = Basket.productListSize

    private lateinit var mapPairToRemove: Pair<Int, MutableList<Product>>
    private var deletedIndex by Delegates.notNull<Int>()

    fun orderButtonClick() {
        basketView.startOrderActivity()
    }

    fun onCreate() {
        updateBasketInfo()
        basketView.showBasket(Basket)
    }

    private fun productsNumberChanged() {
        totalPriceWithDiscount = Basket.totalPriceWithDiscount
        productListSize = Basket.productListSize

        updateBasketInfo()
    }

    fun productNumberIncreased(position: Int) {
        val sameProductList = Basket.getSameProductListByPosition(position)
        val product = sameProductList[0]

        Basket.addProduct(product)
        productsNumberChanged()
        basketView.setSameProductsNumber(position, sameProductList.size)
        basketView.setTotalProductPrice(position, Basket.getTotalDiscountProductPrice(product.id))
        if (product.percentageDiscount > 0) {
            basketView.setTotalProductPriceTitle(position, Basket.getTotalProductPrice(product.id))
        }
    }

    fun productNumberDecreased(position: Int) {
        val sameProductList = Basket.getSameProductListByPosition(position)
        val product = sameProductList[0]

        Basket.removeProductIfAble(product)
        productsNumberChanged()
        basketView.setSameProductsNumber(position, sameProductList.size)
        basketView.setTotalProductPrice(position, Basket.getTotalDiscountProductPrice(product.id))
        if (product.percentageDiscount > 0) {
            basketView.setTotalProductPriceTitle(position, Basket.getTotalProductPrice(product.id))
        }
    }

    private fun updateBasketInfo() {
        val basketAmount = String.format(basketView.context.getString(R.string.price_format), totalPriceWithDiscount)
        basketView.setBasketAmount(basketAmount)
        basketView.setBasketItemsNumber(productListSize.toString())
    }

    fun onProductItemSwiped(position: Int) {
        mapPairToRemove = Basket.productListMap.toList()[position]
        deletedIndex = position

        basketView.removeProductFromList(position)
        updateTotalSizeAndTotalPrice(mapPairToRemove, true)
        updateBasketInfo()
        basketView.setOrderButtonIsEnabled(productListSize > 0)

        val removedItemName = mapPairToRemove.second[0].name
        val undoTitle =
            String.format(basketView.context.getString(R.string.removed_item_snackbar_format), removedItemName)
        basketView.showUndo(undoTitle)

    }

    private fun updateTotalSizeAndTotalPrice(mapPairToRemove: Pair<Int, MutableList<Product>>, isRemoved: Boolean) {//TODO: Move to basket
        if (isRemoved) {
            totalPriceWithDiscount -= mapPairToRemove.second[0].priceWithDiscount * mapPairToRemove.second.size
            productListSize -= mapPairToRemove.second.size
        } else {
            totalPriceWithDiscount += mapPairToRemove.second[0].priceWithDiscount * mapPairToRemove.second.size
            productListSize += mapPairToRemove.second.size
        }
    }

    fun undoPressed() {
        basketView.restoreProduct(mapPairToRemove, deletedIndex)
        updateTotalSizeAndTotalPrice(mapPairToRemove, false)
        basketView.setOrderButtonIsEnabled(productListSize > 0)
        updateBasketInfo()
    }
}