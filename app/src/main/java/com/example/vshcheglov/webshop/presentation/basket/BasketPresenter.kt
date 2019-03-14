package com.example.vshcheglov.webshop.presentation.basket

import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.entites.ProductBasketCard
import com.example.vshcheglov.webshop.presentation.entites.mappers.ProductBasketCardMapper
import kotlin.properties.Delegates

class BasketPresenter(
    private var basketView: BasketView?,
    private val productBasketCardMapper: ProductBasketCardMapper) {

    private lateinit var productToCount: Pair<Product, Int>
    private var deletedIndex by Delegates.notNull<Int>()

    fun makeOrder() {
        basketView?.startOrderActivity()
    }

    fun initProductListWithBasketInfo() {
        updateBasketInfo()
        basketView?.showBasket(productBasketCardMapper.map(Basket))
    }

    fun productNumberIncreased(position: Int) {
        Basket.incrementProductCount(position)
        cardAndBasketUpdate(position)
    }

    fun productNumberDecreased(position: Int) {
        if (Basket.decrementProductCountIfAble(position)) {
            cardAndBasketUpdate(position)
        }
    }

    private fun cardAndBasketUpdate(position: Int) {
        val updatedProductToCount = Basket.productToCountList[position]
        val productCount = updatedProductToCount.second
        val product = updatedProductToCount.first

        updateBasketInfo()

        basketView?.let {
            it.setSameProductsNumber(position, productCount)
            it.setTotalProductPrice(position, Basket.getSameProductDiscountPrice(product.id))
            if (product.percentageDiscount > 0) {
                it.setTotalProductPriceTitle(
                    position,
                    Basket.getSameProductPrice(product.id),
                    product.percentageDiscount.toDouble()
                )
            }
        }
    }


    private fun updateBasketInfo() {
        basketView?.let {
            it.setBasketAmount(Basket.totalPriceWithDiscount)
            it.setBasketItemsNumber(Basket.productsNumber.toString())
        }
    }

    fun removeProductFromBasket(position: Int) {
        productToCount = Basket.productToCountList[position]
        deletedIndex = position

        Basket.removeSameProducts(position)

        val removedItemName = productToCount.first.name
        basketView?.let {
            it.removeProductCard(position)
            it.setOrderButtonIsEnabled(Basket.productsNumber > 0)
            it.showUndo(removedItemName)
        }

        updateBasketInfo()
    }

    fun restoreProductCard() {
        Basket.addProductToCountEntry(productToCount, deletedIndex)
        basketView?.let {
            it.restoreSameProductsCard(deletedIndex)
            it.setOrderButtonIsEnabled(Basket.productsNumber > 0)
        }
        updateBasketInfo()
    }

    fun onDetached() {
        basketView = null
    }

    fun onAttached(basketView: BasketView) {
        this.basketView = basketView
    }

    interface BasketView {
        fun startOrderActivity()

        fun setBasketAmount(amount: Double)

        fun setBasketItemsNumber(itemsNumber: String)

        fun showUndo(productName: String)

        fun showBasket(productBaseketCardList: MutableList<ProductBasketCard>)

        fun setOrderButtonIsEnabled(isEnabled: Boolean)

        fun removeProductCard(position: Int)

        fun restoreSameProductsCard(deletedIndex: Int)

        fun setSameProductsNumber(position: Int, number: Int)

        fun setTotalProductPrice(position: Int, totalDiscountPrice: Double)

        fun setTotalProductPriceTitle(position: Int, totalPrice: Double, percentageDiscount: Double)
    }
}