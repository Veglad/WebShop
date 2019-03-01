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

class BasketRecyclerAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<BasketRecyclerAdapter.ViewHolder>() {

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

    private fun bindWithProduct(
        product: Product,
        view: View,
        sameProductsNumber: Int,
        productList: MutableList<Product>
    ) {
        with(product) {
            Glide.with(view.context)
                .load(imageThumbnailUrl)
                .error(R.drawable.no_image)
                .into(view.basketImage)
            view.basketImage.contentDescription = String.format(
                view.context.getString(R.string.image_content_text_format),
                name
            )
            if (promotional > 0) {
                view.basketSaleTextView.also {
                    it.text = String.format(
                        view.context.getString(R.string.sale_format),
                        promotional
                    )
                    it.visibility = View.VISIBLE
                }
            }
            view.basketItemCountTextView.text = sameProductsNumber.toString()
            view.addImageButton.setOnClickListener {
                Basket.addProduct(product)
                onProductsNumberChangeListener?.invoke()
                view.basketItemCountTextView.text = productList.size.toString()

            }
            view.removeImageButton.setOnClickListener {
                Basket.removeProductIfAble(product)
                onProductsNumberChangeListener?.invoke()
                view.basketItemCountTextView.text = productList.size.toString()
            }

            view.basketTitle.text = name
            view.basketDescription.text = shortDescription

            updatePricesWithProduct(view, product)
        }
    }

    private fun updatePricesWithProduct(view: View, product: Product) {
        if (product.promotional > 0) {
            view.basketFinalPriceTitle.also {
                it.text = String.format(view.context.getString(R.string.price_format), product.price)
                it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
        view.basketFinalPrice.text =
            String.format(view.context.getString(R.string.price_format), product.getPriceWithDiscount())
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}