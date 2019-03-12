package com.example.vshcheglov.webshop.presentation.basket

import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.entites.BasketPresentation
import com.example.vshcheglov.webshop.presentation.entites.ProductPresentation
import com.example.vshcheglov.webshop.presentation.entites.mappers.BasketPresentationMapper
import com.example.vshcheglov.webshop.presentation.entites.mappers.ProductPresentationMapper
import kotlin.properties.Delegates

class BasketPresenter(private val basketView: BasketView) {

    private lateinit var mapPairToRemove: Pair<Int, MutableList<Product>>
    private var deletedIndex by Delegates.notNull<Int>()

    fun makeOrder() {
        basketView.startOrderActivity()
    }

    fun initProductListWithBasketInfo() {
        updateBasketInfo()
        basketView.showBasket(BasketPresentationMapper.transform(Basket))
    }

    fun productNumberIncreased(position: Int) {
        val sameProductList = Basket.getSameProductListByPosition(position)
        val product = sameProductList[0]

        Basket.addProduct(product)
        updateBasketInfo()
        updateProductCard(position, sameProductList, product)
    }

    fun productNumberDecreased(position: Int) {
        val sameProductList = Basket.getSameProductListByPosition(position)
        val product = sameProductList[0]

        if (Basket.removeProductIfAble(product)) {
            updateBasketInfo()
            updateProductCard(position, sameProductList, product)
        }
    }

    private fun updateProductCard(position: Int, sameProductList: MutableList<Product>, product: Product) {
        basketView.setSameProductsNumber(position, sameProductList.size)
        basketView.setTotalProductPrice(position, Basket.getTotalDiscountProductPrice(product.id))
        if (product.percentageDiscount > 0) {
            basketView.setTotalProductPriceTitle(position, Basket.getTotalProductPrice(product.id))
        }
    }

    private fun updateBasketInfo() {
        basketView.setBasketAmount(Basket.totalPriceWithDiscount)
        basketView.setBasketItemsNumber(Basket.productListSize.toString())
    }

    fun removeProductFromBasket(position: Int) {
        mapPairToRemove = Basket.productListMap.toList()[position]
        deletedIndex = position

        Basket.removeSameProducts(position)
        basketView.removeSameProductsCard(position)
        updateBasketInfo()
        basketView.setOrderButtonIsEnabled(Basket.productListSize > 0)

        val removedItemName = mapPairToRemove.second[0].name
        basketView.showUndo(removedItemName)

    }

    fun undoPressed() {
        Basket.addPair(mapPairToRemove, deletedIndex)
        val productToNumberPair =
            Pair(ProductPresentationMapper.transform(mapPairToRemove.second[0]), mapPairToRemove.second.size)
        basketView.restoreSameProductsCard(productToNumberPair, deletedIndex)
        basketView.setOrderButtonIsEnabled(Basket.productListSize > 0)
        updateBasketInfo()
    }

    interface BasketView {
        fun startOrderActivity()

        fun setBasketAmount(amount: Double)

        fun setBasketItemsNumber(itemsNumber: String)

        fun showUndo(productName: String)

        fun showBasket(basket: BasketPresentation)

        fun setOrderButtonIsEnabled(isEnabled: Boolean)

        fun removeSameProductsCard(position: Int)

        fun restoreSameProductsCard(productToNumberPair: Pair<ProductPresentation, Int>, deletedIndex: Int)

        fun setSameProductsNumber(position: Int, number: Int)

        fun setTotalProductPrice(position: Int, totalDiscountPrice: Double)

        fun setTotalProductPriceTitle(position: Int, totalPrice: Double)
    }
}