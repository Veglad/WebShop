package com.example.vshcheglov.webshop.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MenuItem
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.ui.adapters.BasketRecyclerAdapter
import com.example.vshcheglov.webshop.ui.helpers.BasketRecyclerItemTouchHelper
import kotlinx.android.synthetic.main.activity_basket.*

class BasketActivity : AppCompatActivity(), BasketRecyclerItemTouchHelper.BasketRecyclerItemTouchHelperListener {

    lateinit var basketAdapter: BasketRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        initRecyclerView()
        initOrderTextViews(Basket.totalPriceWithDiscount, Basket.productListSize.toString())
        initActionBar()
    }

    private fun initRecyclerView() {
        with(basketRecyclerView) {
            layoutManager = LinearLayoutManager(this@BasketActivity)
            basketAdapter = BasketRecyclerAdapter(this@BasketActivity).also {
                it.onProductsNumberChangeListener = {
                    initOrderTextViews(Basket.totalPriceWithDiscount, Basket.productListSize.toString())
                }
            }
            adapter = basketAdapter
            itemAnimator = DefaultItemAnimator()
            val itemTouchSimpleCallback = BasketRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this@BasketActivity)
            ItemTouchHelper(itemTouchSimpleCallback).attachToRecyclerView(this)
        }
    }

    private fun initOrderTextViews(totalPrice: Double, productListSize: String) {
        basketAmountTextView.text = String.format(getString(R.string.price_format), totalPrice)
        basketItemsTextView.text = productListSize
    }

    private fun initActionBar() {
        supportActionBar?.also {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val holder = viewHolder as? BasketRecyclerAdapter.ViewHolder
        holder?.let {
            val mapPairToRemove = Basket.productListMap.toList()[holder.adapterPosition]
            val removedItemName = mapPairToRemove.second[0].name
            val deletedIndex = viewHolder.adapterPosition

            basketAdapter.removeItem(position)


            val snackbar = Snackbar.make(basketMainConstraint, String.format(getString(R.string.removed_item_snackbar_format), removedItemName),
                Snackbar.LENGTH_SHORT)
            snackbar.setAction(getString(R.string.undo_uppercase)){
                basketAdapter.restoreItem(mapPairToRemove, deletedIndex)
            }
            snackbar.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
