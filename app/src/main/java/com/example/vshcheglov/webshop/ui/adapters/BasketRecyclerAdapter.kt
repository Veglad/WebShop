package com.example.vshcheglov.webshop.ui.adapters

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import kotlinx.android.synthetic.main.basket_recycler_item.view.*

class BasketRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<BasketRecyclerAdapter.ViewHolder>() {

    var onProductsNumberChangeListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.basket_recycler_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = Basket.mapSize

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.view
        val productList = Basket.productListMap.values.toList()[position]
        val sameProductsNumber = productList.size
        if (productList.isNotEmpty()) {
            val product = productList[0]
            bindWithProduct(product, view, sameProductsNumber, productList)
        }
    }

    private fun bindWithProduct(product: Product, view: View,
                                sameProductsNumber: Int, productList: MutableList<Product>) {
        with(product) {
            Glide.with(view.context)
                .load(imageThumbnailUrl)
                .error(R.drawable.no_image)
                .into(view.basketImage)
            view.basketImage.contentDescription = String.format(
                view.context.getString(R.string.image_content_text_format),
                name
            )

            view.basketItemCountTextView.text = sameProductsNumber.toString()
            view.basketTitle.text = name
            view.basketDescription.text = shortDescription

            bindSaleTitle(view, product)
            initClickListeners(view, product, productList, Basket)
            bindPrices(
                view, product, Basket.getTotalProductPrice(product.deviceId),
                Basket.getTotalDiscountProductPrice(product.deviceId)
            )
        }
    }

    private fun initClickListeners(view: View, product: Product, productList: MutableList<Product>, basket: Basket) {
        view.addImageButton.setOnClickListener {
            basket.addProduct(product)
            onProductsNumberChangeListener?.invoke()
            view.basketItemCountTextView.text = productList.size.toString()
            bindPrices(
                view, product, basket.getTotalProductPrice(product.deviceId),
                basket.getTotalDiscountProductPrice(product.deviceId)
            )

        }
        view.removeImageButton.setOnClickListener {
            basket.removeProductIfAble(product)
            onProductsNumberChangeListener?.invoke()
            view.basketItemCountTextView.text = productList.size.toString()
            bindPrices(
                view, product, basket.getTotalProductPrice(product.deviceId),
                basket.getTotalDiscountProductPrice(product.deviceId)
            )
        }
    }

    private fun bindSaleTitle(view: View, product: Product) {
        if (product.promotional > 0) {
            view.basketSaleTextView.text = String.format(
                view.context.getString(R.string.sale_format),
                product.promotional
            )
        } else {
            view.basketSaleTextView.visibility = View.INVISIBLE
        }
    }

    private fun bindPrices(
        view: View, product: Product, totalPrice: Double,
        totalDiscountPrice: Double
    ) {
        if (product.promotional > 0) {
            view.basketProductPriceTitle.also {
                it.text = String.format(view.context.getString(R.string.price_format), product.price)
                it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            view.basketTotalPriceTitle.also {
                it.text = String.format(view.context.getString(R.string.price_format), totalPrice)
                it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

        view.basketPriceTextView.text =
            String.format(view.context.getString(R.string.price_format), product.priceWithDiscount)
        view.basketTotalPrice.text =
            String.format(view.context.getString(R.string.price_format), totalDiscountPrice)
    }

    fun removeItem(position: Int) {
        Basket.removeSameProducts(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(pairProduct: Pair<Int, MutableList<Product>>, position: Int) {
        Basket.addPair(pairProduct, position)
        notifyItemInserted(position)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}