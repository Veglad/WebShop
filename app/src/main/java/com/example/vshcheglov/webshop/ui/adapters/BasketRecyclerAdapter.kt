package com.example.vshcheglov.webshop.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import kotlinx.android.synthetic.main.basket_recycler_item.view.*

class BasketRecyclerAdapter(private val context: Context, var productList: List<Product>) :
    RecyclerView.Adapter<BasketRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.basket_recycler_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(productList[position]) {
            Glide.with(view.context)
                .load(imageThumbnailUrl)
                .error(R.drawable.no_image)
                .into(view.basketImage)
            view.basketImage.contentDescription = String.format(
                view.context.getString(R.string.image_content_text_format),
                name
            )
            holder.view.basketSaleTextView.text = String.format(
                holder.view.context.getString(com.example.vshcheglov.webshop.R.string.sale_format),
                promotional
            )
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