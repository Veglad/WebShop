package com.example.vshcheglov.webshop.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.ui.adapters.BasketRecyclerAdapter
import kotlinx.android.synthetic.main.activity_basket.*

class BasketActivity : AppCompatActivity() {

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
            adapter = BasketRecyclerAdapter(this@BasketActivity).also {
                it.onProductsNumberChangeListener = {
                    initOrderTextViews(Basket.totalPriceWithDiscount, Basket.productListSize.toString())
                }
            }
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
