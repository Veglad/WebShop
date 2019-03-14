package com.example.vshcheglov.webshop.presentation.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.vshcheglov.webshop.BuildConfig
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.example.vshcheglov.webshop.presentation.App
import com.example.vshcheglov.webshop.presentation.main.adapters.ProductsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main_primary.*
import kotlinx.android.synthetic.main.activity_main_error_layout.*
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainPresenter.MainView {

    private lateinit var productsRecyclerAdapter: ProductsRecyclerAdapter

    @Inject lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).productsComponent.inject(this)
        setContentView(R.layout.activity_main)
        mainPresenter.onAttached(this)

        initTimberLogging()
        mainPresenter.loadProducts(isNetworkAvailable())

        tryAgainButton.setOnClickListener {
            Timber.d("Try again button clicked")
            mainPresenter.loadProducts(isNetworkAvailable())
        }
        productsSwipeRefreshLayout.setOnRefreshListener {
            Timber.d("Refresh data triggered")
            mainPresenter.loadProducts(isNetworkAvailable())
        }

        productsRecyclerAdapter = ProductsRecyclerAdapter(this, mutableListOf(), mutableListOf())
        with(productsRecyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productsRecyclerAdapter
        }
    }

    private fun initTimberLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onDestroy() {
        mainPresenter.clearResources()
        mainPresenter.onDetached()
        super.onDestroy()
    }

    override fun showLoading() {
        productsSwipeRefreshLayout.isRefreshing = true
        productsRecyclerView.visibility = View.INVISIBLE
    }

    override fun hideLoading() {
        productsSwipeRefreshLayout.isRefreshing = false
        productsRecyclerView.visibility = View.VISIBLE
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
