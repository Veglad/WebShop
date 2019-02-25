package com.example.vshcheglov.webshop.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_recycler_item.view.*

class ProductsRecyclerAdapter(private val productList: List<Product>)
    : RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_recycler_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = productList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindProduct(productList[position])
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindProduct(product: Product) {
            with(product) {
                Picasso.get().load(imageUrl).into(view.productImage)
                view.productImage.contentDescription = String.format(view.context.getString(R.string.image_content_text_format), name)
                view.productTitle.text = name
                view.productDescription.text = description
                view.productPrice.text = String.format(view.context.getString(R.string.price_format), price)
            }
        }
    }
}