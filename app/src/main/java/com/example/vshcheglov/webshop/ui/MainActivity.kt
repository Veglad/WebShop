package com.example.vshcheglov.webshop.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.data.NetworkService
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.ui.adapters.ProductsRecyclerAdapter
import com.example.vshcheglov.webshop.utils.isNetworkAvailable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_primary.*
import kotlinx.android.synthetic.main.activity_main_error_layout.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var productsRecyclerAdapter: ProductsRecyclerAdapter
    private val compositeDisposable = CompositeDisposable()
    private var productList = mutableListOf<Product>()

    private var isActivityActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadProductsIfInternetAvailable()
    }

    private fun loadProductsIfInternetAvailable() {
        if (isNetworkAvailable(this)) {
            activityMainPrimaryLayout.visibility = View.VISIBLE
            activityMainErrorLayout.visibility = View.GONE
            productsRecyclerAdapter = ProductsRecyclerAdapter(productList)
            with(productsRecyclerView) {
                layoutManager =  LinearLayoutManager(this@MainActivity)
                adapter = productsRecyclerAdapter
            }
            initSwipeRefresh()
            fetchProducts()
        } else {
            activityMainErrorLayout.visibility = View.VISIBLE
            activityMainPrimaryLayout.visibility = View.GONE
            tryAgainButton.setOnClickListener {
                loadProductsIfInternetAvailable()
            }
        }
    }

    private fun initSwipeRefresh() {
        productsSwipeRefreshLayout.setOnRefreshListener {
            fetchProducts()
        }
    }

    private fun fetchProducts() {
        productsSwipeRefreshLayout.isRefreshing = true
        val disposable = NetworkService.getAllDevices()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<Product>>() {
                override fun onSuccess(productList: List<Product>) {
                    if(isActivityActive) {
                        productsRecyclerAdapter.productList = productList
                        productsRecyclerAdapter.notifyDataSetChanged()
                        productsSwipeRefreshLayout.isRefreshing = false
                    }
                }

                override fun onError(e: Throwable) {
                    if(isActivityActive) {
                        Toast.makeText(this@MainActivity, resources.getString(R.string.loading_products_error), Toast.LENGTH_SHORT).show()
                        productsSwipeRefreshLayout.isRefreshing = false
                    }
                }

            })
        compositeDisposable.add(disposable)
    }

    override fun onPause() {
        super.onPause()
        isActivityActive = false
    }

    override fun onResume() {
        super.onResume()
        isActivityActive = true
        loadProductsIfInternetAvailable()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
