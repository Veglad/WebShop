package com.example.vshcheglov.webshop.presentation.order

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.widget.Toast
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.example.vshcheglov.webshop.presentation.main.MainActivity
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.activity_register.*
import nucleus5.factory.RequiresPresenter
import nucleus5.view.NucleusAppCompatActivity

@RequiresPresenter(OrderPresenter::class)
class OrderActivity : NucleusAppCompatActivity<OrderPresenter>(), OrderPresenter.OrderView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        buttonOrder.setOnClickListener {
            val name = orderName.text.toString()
            val lastName = orderLastName.text.toString()
            val cardNumber = orderCardNumber.text.toString()
            val cardMonth = orderCardMonth.text.toString().toIntOrNull()
            val cardYear = orderCardYear.text.toString().toIntOrNull()
            val cardCv = orderCardCV.text.toString()

            clearErrors()

            presenter.makeOrder(name, lastName, cardNumber, cardMonth, cardYear, cardCv, isNetworkAvailable())
        }

        initActionBar()
    }

    private fun clearErrors() {
        nameTextInput.error = ""
        lastNameTextInput.error = ""
        cardNumberTextInput.error = ""
        cardMonthTextInput.error = ""
        cardYearTextInput.error = ""
        cardCvTextInput.error = ""
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

    override fun setShowProgress(isVisible: Boolean) {
        if (isVisible) {
            buttonOrder.startAnimation()
        } else {
            buttonOrder.revertAnimation()
        }
    }

    override fun showInvalidName() {
        nameTextInput.error = resources.getString(R.string.order_invalid_name)
    }

    override fun showInvalidSecondName() {
        lastNameTextInput.error = resources.getString(R.string.order_invalid_last_name)
    }

    override fun showInvalidCardNumber() {
        cardNumberTextInput.error = resources.getString(R.string.order_invalid_card_number)
    }

    override fun showInvalidCardMonth() {
        cardMonthTextInput.error = resources.getString(R.string.order_invalid_card_month)
    }

    override fun showInvalidCardYear() {
        cardYearTextInput.error = resources.getString(R.string.order_invalid_card_year)
    }

    override fun showInvalidCardCv() {
        cardCvTextInput.error = resources.getString(R.string.order_invalid_cv)
    }

    override fun showNoInternetError() {
        Snackbar.make(
            orderLinearLayout,
            resources.getString(R.string.no_internet_connection_warning),
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun notifyOrderCompleted() {
        Toast.makeText(this, getString(R.string.order_completed), Toast.LENGTH_LONG).show()
    }

    override fun showOrderSaveError() {
        Snackbar.make(orderLinearLayout, getString(R.string.order_error), Snackbar.LENGTH_SHORT).show()
    }

    override fun startMainScreen() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}
