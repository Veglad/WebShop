package com.example.vshcheglov.webshop.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Basket
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        orderTotalPrice.text = String.format(getString(R.string.price_format), Basket.totalPriceWithDiscount)
        buttonOrder.setOnClickListener {
            Snackbar.make(orderConstraintLayout, getString(R.string.order_completed),
                Snackbar.LENGTH_SHORT).show()
        }
    }
}
