package com.example.vshcheglov.webshop.presentation.bought

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.data.enteties.OrderProduct
import com.google.firebase.Timestamp
import com.kinda.alert.KAlertDialog
import kotlinx.android.synthetic.main.activity_bought.*
import nucleus5.factory.RequiresPresenter
import nucleus5.view.NucleusAppCompatActivity

@RequiresPresenter(BoughtPresenter::class)
class BoughtActivity : NucleusAppCompatActivity<BoughtPresenter>(), BoughtPresenter.View {

    companion object {
        const val COLUMNS_NUMBER = 2
    }

    private lateinit var boughtRecyclerAdapter: BoughtRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bought)

        setSupportActionBar(boughtToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setTitle(R.string.bought_products)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }

        return false
    }

    override fun showProducts(productToTimeStampList: List<Pair<OrderProduct, Timestamp>>) {
        boughtRecyclerAdapter = BoughtRecyclerAdapter(this, productToTimeStampList)
        boughtRecyclerView.layoutManager = GridLayoutManager(this, COLUMNS_NUMBER)
        boughtRecyclerView.adapter = boughtRecyclerAdapter
    }

    override fun showProductsFetchingError() {
        KAlertDialog(this, KAlertDialog.ERROR_TYPE)
            .setTitleText(getString(R.string.bought_error_title))
            .setContentText(getString(R.string.bought_error_message))
            .setConfirmText(getString(R.string.ok))
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
            }
            .show()
    }
}
