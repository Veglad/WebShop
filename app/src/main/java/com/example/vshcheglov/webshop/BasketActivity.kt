package com.example.vshcheglov.webshop

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.ui.adapters.BasketRecyclerAdapter
import kotlinx.android.synthetic.main.activity_basket.*

class BasketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        initRecyclerView()
        initOrderTextViews()
    }

    private fun initRecyclerView() {
        with(basketRecyclerView) {
            layoutManager = LinearLayoutManager(this@BasketActivity)
            adapter = BasketRecyclerAdapter(this@BasketActivity, Basket.productList)
        }
    }

    private fun initOrderTextViews() {
        basketAmountTextView.text = String.format(getString(R.string.price_format), Basket.totalPrice)
        basketItemsTextView.text = Basket.size.toString()
    }
}
