package com.example.vshcheglov.webshop.presentation.order

import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.example.vshcheglov.webshop.R
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity(), OrderPresenter.OrderView {

    private val orderPresenter = OrderPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        orderPresenter.initOrderPrice()

        buttonOrder.setOnClickListener {
            Snackbar.make(orderConstraintLayout, getString(R.string.order_completed), Snackbar.LENGTH_SHORT).show()
        }

        initActionBar()
    }

    private fun initActionBar() {
        setSupportActionBar(orderActionBar)
        supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }
        orderActionBar.navigationIcon?.setColorFilter(
            ContextCompat.getColor(this, R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }

        return false
    }

    override fun setOrderPrice(orderPrice: Double) {
        orderTotalPrice.text = String.format(getString(R.string.price_format), orderPrice)
    }
}
