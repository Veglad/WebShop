package com.example.vshcheglov.webshop.presentation.main

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.example.vshcheglov.webshop.presentation.main.adapters.ProductsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main_primary.*
import kotlinx.android.synthetic.main.activity_main_error_layout.*
import kotlinx.android.synthetic.main.activity_main.*
import nucleus5.factory.RequiresPresenter
import nucleus5.view.NucleusAppCompatActivity
import timber.log.Timber

@RequiresPresenter(MainPresenter::class)
class MainActivity : NucleusAppCompatActivity<MainPresenter>(), MainPresenter.MainView {

    private lateinit var productsRecyclerAdapter: ProductsRecyclerAdapter
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter?.loadProducts(isNetworkAvailable())

        tryAgainButton.setOnClickListener {
            val isNetworkAvailable = isNetworkAvailable()

            showErrorScreen(!isNetworkAvailable)
            presenter?.loadProducts(isNetworkAvailable)
            if(isNetworkAvailable) {
                snackbar?.dismiss()
            }
        }
        productsSwipeRefreshLayout.setOnRefreshListener {
            val isNetworkAvailable = isNetworkAvailable()

            Timber.d("Refresh data triggered")
            presenter?.loadProducts(isNetworkAvailable)
            if(isNetworkAvailable) {
                snackbar?.dismiss()
            }
        }

        productsRecyclerAdapter = ProductsRecyclerAdapter(this, mutableListOf(), mutableListOf())
        with(productsRecyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productsRecyclerAdapter
        }
    }

    override fun showLoading(isLoading: Boolean) {
        productsSwipeRefreshLayout.isRefreshing = isLoading
        if (isLoading) {
            productsRecyclerView.visibility = View.INVISIBLE
        } else {
            productsRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun showNoInternetWarning() {
        val isNetworkAvailable = isNetworkAvailable()
        snackbar = Snackbar.make(mainFrameLayout,
            getString(R.string.no_internet_connection_warning), Snackbar.LENGTH_INDEFINITE)
        snackbar?.setAction(getString(R.string.try_again_button)) {
                if (isNetworkAvailable) {
                    showErrorScreen(false)
                }

                presenter?.loadProducts(isNetworkAvailable)
                snackbar?.dismiss()
            }
        snackbar?.show()
    }

    override fun showError(throwable: Throwable) {
        showErrorScreen(true)
        showNoInternetWarning()
    }

    override fun showProductList(productList: List<Product>) {
        showErrorScreen(false)
        productsRecyclerAdapter.productList = productList
        productsRecyclerAdapter.notifyDataSetChanged()
    }

    override fun showPromotionalProductList(promotionalList: List<Product>) {
        showErrorScreen(false)
        productsRecyclerAdapter.updatePromotionalList(promotionalList)
    }

    private fun showErrorScreen(isVisible: Boolean) {
        activityMainErrorLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
        activityMainPrimaryLayout.visibility = if (isVisible) View.GONE else View.VISIBLE
    }
}
