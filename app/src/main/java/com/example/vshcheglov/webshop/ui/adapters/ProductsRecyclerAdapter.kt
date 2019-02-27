package com.example.vshcheglov.webshop.ui.adapters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import kotlinx.android.synthetic.main.product_recycler_item.view.*
import kotlinx.android.synthetic.main.products_recycler_title.view.*
import kotlinx.android.synthetic.main.promotional_recycler_view.view.*

class ProductsRecyclerAdapter(
    private val context: Context,
    var productList: List<Product>,
    var promotionalProductList: List<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val PROMOTIONAL_POSITION = 1
        private const val PROMOTIONAL_TITLE_POSITION = 0
        private const val PRODUCTS_TITLE_POSITION = 2

        private const val TITLE_TYPE = 0
        private const val PROMOTIONAL_DEVICES_TYPE = 1
        private const val DEVICES_TYPE = 2

        private const val NOT_PRODUCTS_IN_LIST_COUNT = 3
    }

    private val promotionalRecyclerAdapter by lazy {
        PromotionalRecyclerAdapter(promotionalProductList)
    }

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        TITLE_TYPE -> {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.products_recycler_title, parent, false)
            TitleViewHolder(view)
        }
        PROMOTIONAL_DEVICES_TYPE -> {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.promotional_recycler_view, parent, false)
            PromotionalViewHolder(view)
        }
        else -> {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_recycler_item, parent, false)
            ProductsViewHolder(view)
        }
    }

    override fun getItemCount() = productList.size + NOT_PRODUCTS_IN_LIST_COUNT

    override fun getItemViewType(position: Int) = when (position) {
        PROMOTIONAL_TITLE_POSITION, PRODUCTS_TITLE_POSITION -> TITLE_TYPE
        PROMOTIONAL_POSITION -> PROMOTIONAL_DEVICES_TYPE
        else -> DEVICES_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            TITLE_TYPE -> {
                bindTitles(holder, position)
            }
            PROMOTIONAL_DEVICES_TYPE -> {
                bindPromotionalList(holder)
            }
            else -> {
                bindProductsList(holder, position)
            }
        }
    }

    private fun bindTitles(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TitleViewHolder).apply {
            view.productsRecyclerTitleTextView.text = if (position == 0) {
                view.context.getString(R.string.promotional_title)
            } else {
                view.context.getString(R.string.devices_title)
            }
        }
    }

    private fun bindPromotionalList(holder: RecyclerView.ViewHolder) {
        val promotionalViewHolder = (holder as PromotionalViewHolder)
        val horizontalManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        with(promotionalViewHolder.view.promotionalRecyclerView) {
            setRecycledViewPool(viewPool)
            layoutManager = horizontalManager
            adapter = promotionalRecyclerAdapter
            onFlingListener = null
            LinearSnapHelper().attachToRecyclerView(this)
        }
    }

    private fun bindProductsList(holder: RecyclerView.ViewHolder, position: Int) {
        val view = (holder as ProductsViewHolder).view
        val positionInProductList = position - NOT_PRODUCTS_IN_LIST_COUNT
        with(productList[positionInProductList]) {
            Glide.with(view.context)
                .load(imageUrl)
                .error(R.drawable.no_image)
                .into(view.productImage)
            view.productImage.contentDescription = String.format(
                view.context.getString(R.string.image_content_text_format),
                name
            )
            view.productTitle.text = name
            view.productDescription.text = description
            view.productPrice.text = String.format(
                view.context.getString(R.string.price_format),
                price
            )
        }
    }

    fun updatePromotionalList(promotionalProductList: List<Product>) {
        this.promotionalProductList = promotionalProductList
        promotionalRecyclerAdapter.productList = promotionalProductList
        promotionalRecyclerAdapter.notifyDataSetChanged()
        notifyItemChanged(PROMOTIONAL_POSITION)
    }

    class ProductsViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class PromotionalViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}