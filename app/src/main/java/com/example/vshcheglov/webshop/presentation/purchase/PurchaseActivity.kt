package com.example.vshcheglov.webshop.presentation.purchase

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.OrderProduct
import com.example.vshcheglov.webshop.presentation.main.MainActivity
import com.google.firebase.Timestamp
import com.kinda.alert.KAlertDialog
import kotlinx.android.synthetic.main.activity_bought.*
import kotlinx.android.synthetic.main.purchase_error_layout.*
import kotlinx.android.synthetic.main.purchase_list_layout.*
import nucleus5.factory.RequiresPresenter
import nucleus5.view.NucleusAppCompatActivity

@RequiresPresenter(PurchasePresenter::class)
class PurchaseActivity : NucleusAppCompatActivity<PurchasePresenter>(), PurchasePresenter.View {

    companion object {
        const val COLUMNS_NUMBER = 2
    }

    private lateinit var boughtRecyclerAdapter: PurchaseRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bought)

        setSupportActionBar(boughtToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setTitle(R.string.bought_products)
        }

        purchaseErrorLayoutButton.setOnClickListener { startMainActivity() }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }

        return false
    }

    override fun showProducts(productToTimeStampList: List<Pair<OrderProduct, Timestamp>>) {
        boughtRecyclerAdapter = PurchaseRecyclerAdapter(this, productToTimeStampList)
        purchaseRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, COLUMNS_NUMBER)
        purchaseRecyclerView.adapter = boughtRecyclerAdapter
    }

    override fun showProductsFetchingError(exception: Exception) {
        KAlertDialog(this, KAlertDialog.ERROR_TYPE)
            .setTitleText(getString(R.string.bought_error_title))
            .setContentText(getString(R.string.bought_error_message))
            .setConfirmText(getString(R.string.ok))
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
                startMainActivity()
            }
            .show()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    override fun showNoData() {
        purchaseListLayout.visibility = View.GONE
        purchaseErrorLayout.visibility = View.VISIBLE
    }

    override fun setShowLoading(isLoading: Boolean) {
        purchaseProgressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }
}
