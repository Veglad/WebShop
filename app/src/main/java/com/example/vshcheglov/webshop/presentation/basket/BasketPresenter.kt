package com.example.vshcheglov.webshop.presentation.basket

import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.entites.mappers.BasketPresentationMapper
import com.example.vshcheglov.webshop.presentation.entites.mappers.ProductPresentationMapper
import kotlin.properties.Delegates

class BasketPresenter(private val basketView: IBasketView) {

    private lateinit var mapPairToRemove: Pair<Int, MutableList<Product>>
    private var deletedIndex by Delegates.notNull<Int>()

    fun orderButtonClick() {
        basketView.startOrderActivity()
    }

    fun onCreate() {
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

        if(Basket.removeProductIfAble(product)) {
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
        val basketAmount = String.format(basketView.context.getString(R.string.price_format), Basket.totalPriceWithDiscount)
        basketView.setBasketAmount(basketAmount)
        basketView.setBasketItemsNumber(Basket.productListSize.toString())
    }

    fun onProductItemSwiped(position: Int) {
        mapPairToRemove = Basket.productListMap.toList()[position]
        deletedIndex = position

        Basket.removeSameProducts(position)
        basketView.removeSameProductsCard(position)
        updateBasketInfo()
        basketView.setOrderButtonIsEnabled(Basket.productListSize > 0)

        val removedItemName = mapPairToRemove.second[0].name
        val undoTitle =
            String.format(basketView.context.getString(R.string.removed_item_snackbar_format), removedItemName)
        basketView.showUndo(undoTitle)

    }

    fun undoPressed() {
        Basket.addPair(mapPairToRemove, deletedIndex)
        val productToNumberPair =
            Pair(ProductPresentationMapper.transform(mapPairToRemove.second[0]), mapPairToRemove.second.size)
        basketView.restoreSameProductsCard(productToNumberPair, deletedIndex)
        basketView.setOrderButtonIsEnabled(Basket.productListSize > 0)
        updateBasketInfo()
    }
}