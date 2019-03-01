package com.example.vshcheglov.webshop.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import kotlinx.android.synthetic.main.basket_recycler_item.view.*

class BasketRecyclerAdapter(
    private val context: Context,
    var productListMap: LinkedHashMap<Int, MutableList<Product>>
) :
    RecyclerView.Adapter<BasketRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.basket_recycler_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = productListMap.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.view
        val productsMapEntry = productListMap.values.toList()[position]
        if (productsMapEntry.isNotEmpty()) {
            val sameProductsNumber = productsMapEntry.size
            val product = productsMapEntry[0]
            bindWithProduct(product, view, sameProductsNumber)
        }

    }

    private fun bindWithProduct(product: Product, view: View, sameProductsNumber: Int) {
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
            if (sameProductsNumber > 1) {
                view.basketItemCountTitle.visibility = View.VISIBLE
                view.basketItemCountTextView.also {
                    it.visibility = View.VISIBLE
                    it.text = sameProductsNumber.toString()
                }
            }

            view.basketTitle.text = name
            view.basketDescription.text = shortDescription
            view.basketFinalPrice.text = String.format(
                view.context.getString(R.string.price_format),
                price
            )
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}