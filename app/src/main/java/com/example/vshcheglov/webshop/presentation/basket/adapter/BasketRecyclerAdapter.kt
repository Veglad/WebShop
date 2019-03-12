package com.example.vshcheglov.webshop.presentation.basket.adapter

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.presentation.entites.BasketPresentation
import com.example.vshcheglov.webshop.presentation.entites.ProductPresentation
import kotlinx.android.synthetic.main.basket_recycler_item.view.*

class BasketRecyclerAdapter(private val context: Context, val basket: BasketPresentation) :
    RecyclerView.Adapter<BasketRecyclerAdapter.ViewHolder>() {

    var onProductNumberIncreasedListener: ((Int) -> Unit)? = null
    var onProductNumberDecreasedListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.basket_recycler_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = basket.cardsNumber

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.view
        if (basket.listOfProductLists.isNotEmpty()) {
            val productToNumberPair = basket.listOfProductLists[position]
            bindWithProduct(position, view, productToNumberPair)
        }
    }

    private fun bindWithProduct(position: Int, view: View, productToNumberPair: Pair<ProductPresentation, Int>) {
        with(productToNumberPair.first) {
            Glide.with(view.context)
                .load(imageThumbnailUrl)
                .error(R.drawable.no_image)
                .into(view.basketImage)
            view.basketImage.contentDescription = String.format(
                view.context.getString(R.string.image_content_text_format),
                name
            )

            view.basketItemCountTextView.text = productToNumberPair.second.toString()
            view.basketTitle.text = name
            view.basketDescription.text = shortDescription

            view.addImageButton.setOnClickListener { onProductNumberIncreasedListener?.invoke(position) }
            view.removeImageButton.setOnClickListener { onProductNumberDecreasedListener?.invoke(position) }

            initSaleTitle(view, this)
            initProductPrice(view, this)
            initTotalProductsPrice(view, basket.totalPriceDiscount)
            if (productToNumberPair.first.percentageDiscount > 0) {
                initTotalProductsPriceTitle(view, basket.totalPriceDiscount)
            }
        }
    }

    private fun initProductPrice(view: View, product: ProductPresentation) {
        view.basketPriceTextView.text =
            String.format(view.context.getString(R.string.price_format), product.priceWithDiscount)
        //Product price title
        if (product.percentageDiscount > 0) {
            view.basketProductPriceTitle.also {
                it.text = String.format(view.context.getString(R.string.price_format), product.price)
                it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    private fun initSaleTitle(view: View, product: ProductPresentation) {
        view.basketSaleTextView.text =
            String.format(view.context.getString(R.string.sale_format), product.percentageDiscount)
        if (product.percentageDiscount == 0) {
            view.basketSaleTextView.visibility = View.INVISIBLE
        }
    }

    fun initTotalProductsPriceTitle(view: View, totalPrice: Double) {
        view.basketTotalPriceTitle.also {
            it.text = String.format(view.context.getString(R.string.price_format), totalPrice)
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    fun initTotalProductsPrice(view: View, totalDiscountPrice: Double) {
        view.basketTotalPrice.text =
            String.format(view.context.getString(R.string.price_format), totalDiscountPrice)
    }

    fun removeItem(position: Int) {
        basket.cardsNumber--//TODO: Move to helper
        basket.productsNumber -= basket.listOfProductLists[position].second
        basket.listOfProductLists.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(productToNumberPair: Pair<ProductPresentation, Int>, position: Int) {
        basket.cardsNumber++//TODO: Move to helper
        basket.productsNumber += productToNumberPair.second
        basket.listOfProductLists.add(position, productToNumberPair)
        notifyItemInserted(position)
    }

    fun setProductsNumberByPosition(view: View, number: Int, position: Int) {
        basket.productsNumber += number - basket.listOfProductLists[position].second//TODO: Move to helper
        basket.listOfProductLists[position] = Pair(basket.listOfProductLists[position].first, number)
        view.basketItemCountTextView.text = number.toString()
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}