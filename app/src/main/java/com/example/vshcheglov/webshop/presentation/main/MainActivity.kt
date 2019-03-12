package com.example.vshcheglov.webshop.presentation.main

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.main.adapters.ProductsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main_primary.*
import kotlinx.android.synthetic.main.activity_main_error_layout.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainPresenter.MainView {

    private lateinit var productsRecyclerAdapter: ProductsRecyclerAdapter
    private var mainPresenter = MainPresenter(this)

    override val context: Context
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainPresenter.onCreate()

        tryAgainButton.setOnClickListener {
            mainPresenter.tryAgainButtonClick()
        }
        productsSwipeRefreshLayout.setOnRefreshListener {
            mainPresenter.onRefresh()
        }

        productsRecyclerAdapter = ProductsRecyclerAdapter(this, mutableListOf(), mutableListOf())
        with(productsRecyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productsRecyclerAdapter
        }
    }

    override fun onDestroy() {
        mainPresenter.onDestroy()
        super.onDestroy()
    }

    override fun showLoading() {
        productsSwipeRefreshLayout.isRefreshing = true
        productsRecyclerView.visibility = View.INVISIBLE
    }

    override fun hideLoading() {
        if (!isFinishing) {
            productsSwipeRefreshLayout.isRefreshing = false
            productsRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun setShowRetry(isVisible: Boolean) {
        activityMainErrorLayout.visibility = if(isVisible) View.VISIBLE else View.GONE
        activityMainPrimaryLayout.visibility = if(isVisible) View.GONE else View.VISIBLE
    }

    override fun showError(errorMessage: String) {
        if (!isFinishing) {
            Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showProductList(productList: List<Product>) {
        productsRecyclerAdapter.productList = productList
        productsRecyclerAdapter.notifyDataSetChanged()
    }

    override fun showPromotionalProductList(promotionalList: List<Product>) {
        productsRecyclerAdapter.updatePromotionalList(promotionalList)
    }
}
