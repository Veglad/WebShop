package com.example.vshcheglov.webshop.presentation.basket

import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.entites.ProductBasketCard
import com.example.vshcheglov.webshop.presentation.entites.mappers.ProductBasketCardMapper
import kotlin.properties.Delegates

class BasketPresenter(private val basketView: BasketView) {

    private lateinit var removedIdToProductListPair: Pair<Int, MutableList<Product>>
    private var deletedIndex by Delegates.notNull<Int>()

    fun makeOrder() {
        basketView.startOrderActivity()
    }

    fun initProductListWithBasketInfo() {
        updateBasketInfo()
        basketView.showBasket(ProductBasketCardMapper.transform(Basket))
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
            basketView.setTotalProductPriceTitle(position, Basket.getTotalProductPrice(product.id), product.percentageDiscount.toDouble())
        }
    }

    private fun updateBasketInfo() {
        basketView.setBasketAmount(Basket.totalPriceWithDiscount)
        basketView.setBasketItemsNumber(Basket.productListSize.toString())
    }

    fun removeProductFromBasket(position: Int) {
        removedIdToProductListPair = Basket.productListMap.toList()[position]
        deletedIndex = position

        Basket.removeSameProducts(position)
        basketView.removeProductCard(position)
        updateBasketInfo()
        basketView.setOrderButtonIsEnabled(Basket.productListSize > 0)

        val removedItemName = removedIdToProductListPair.second[0].name
        basketView.showUndo(removedItemName)

    }

    fun undoPressed() {
        Basket.addPair(removedIdToProductListPair, deletedIndex)
        basketView.restoreSameProductsCard(deletedIndex)
        basketView.setOrderButtonIsEnabled(Basket.productListSize > 0)
        updateBasketInfo()
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