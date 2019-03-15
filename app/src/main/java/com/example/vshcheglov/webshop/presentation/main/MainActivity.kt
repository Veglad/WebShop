package com.example.vshcheglov.webshop.presentation.main

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter?.loadProducts(isNetworkAvailable())

        tryAgainButton.setOnClickListener {
            Timber.d("Try again button clicked")
            presenter?.loadProducts(isNetworkAvailable())
        }
        productsSwipeRefreshLayout.setOnRefreshListener {
            Timber.d("Refresh data triggered")
            presenter?.loadProducts(isNetworkAvailable())
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

    override fun setShowRetry(isVisible: Boolean) {
        activityMainErrorLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
        activityMainPrimaryLayout.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    override fun showError(throwable: Throwable) {
        val message = getString(R.string.loading_products_error)
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showProductList(productList: List<Product>) {
        productsRecyclerAdapter.productList = productList
        productsRecyclerAdapter.notifyDataSetChanged()
    }

    override fun showPromotionalProductList(promotionalList: List<Product>) {
        productsRecyclerAdapter.updatePromotionalList(promotionalList)
    }
}
