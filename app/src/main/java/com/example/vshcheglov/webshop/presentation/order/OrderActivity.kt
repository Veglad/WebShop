package com.example.vshcheglov.webshop.presentation.order

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.example.vshcheglov.webshop.R
import kotlinx.android.synthetic.main.activity_order.*
import nucleus.factory.RequiresPresenter
import nucleus.view.NucleusAppCompatActivity

@RequiresPresenter(OrderPresenter::class)
class OrderActivity : NucleusAppCompatActivity<OrderPresenter>(), OrderPresenter.OrderView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        buttonOrder.setOnClickListener {
            Snackbar.make(orderConstraintLayout, getString(R.string.order_completed), Snackbar.LENGTH_SHORT).show()
        }

        initActionBar()
    }

    override fun onResume() {
        super.onResume()
        presenter?.initOrderPrice()
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
