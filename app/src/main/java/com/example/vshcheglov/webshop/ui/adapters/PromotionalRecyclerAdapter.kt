package com.example.vshcheglov.webshop.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import kotlinx.android.synthetic.main.product_recycler_item.view.*

class PromotionalRecyclerAdapter(var productList: List<Product>) :
    RecyclerView.Adapter<PromotionalRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.promotional_recycler_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(productList[position]) {
            Glide.with(holder.view.context)
                .load(imageUrl)
                .error(R.drawable.no_image)
                .into(holder.view.productImage)
            holder.view.productImage.contentDescription = String.format(
                holder.view.context.getString(com.example.vshcheglov.webshop.R.string.image_content_text_format),
                name
            )
            holder.view.productTitle.text = name
            holder.view.productPrice.text = String.format(
                holder.view.context.getString(com.example.vshcheglov.webshop.R.string.price_format),
                price
            )
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}