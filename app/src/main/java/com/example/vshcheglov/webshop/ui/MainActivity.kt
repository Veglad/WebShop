package com.example.vshcheglov.webshop.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.data.NetworkService
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.example.vshcheglov.webshop.ui.adapters.ProductsRecyclerAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_primary.*
import kotlinx.android.synthetic.main.activity_main_error_layout.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var productsRecyclerAdapter: ProductsRecyclerAdapter
    private val compositeDisposable = CompositeDisposable()
    private var productList = mutableListOf<Product>()
    private var promotionalList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        loadProductsIfInternetAvailable()
    }

    private fun loadProductsIfInternetAvailable() {
        if (isNetworkAvailable()) {
            activityMainPrimaryLayout.visibility = View.VISIBLE
            activityMainErrorLayout.visibility = View.GONE
            productsRecyclerAdapter = ProductsRecyclerAdapter(this, productList, promotionalList)
            with(productsRecyclerView) {
                layoutManager = LinearLayoutManager(this@MainActivity)
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
        val diposable = Single.zip(NetworkService.getAllDevices(), NetworkService.getAllPromotionalDevices()
            , BiFunction { products: List<Product>, promotionals: List<Product> ->
                Pair(products, promotionals)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Pair<List<Product>, List<Product>>>() {
                override fun onSuccess(pairProducts: Pair<List<Product>, List<Product>>) {
                    if (!isFinishing) {
                        productsSwipeRefreshLayout.isRefreshing = false

                        productsRecyclerAdapter.productList = pairProducts.first
                        productsRecyclerAdapter.notifyDataSetChanged()

                        val promotionalList = pairProducts.second.filter { it.promotional > 0 }
                        productsRecyclerAdapter.updatePromotionalList(promotionalList)
                    }
                }

                override fun onError(e: Throwable) {
                    if (!isFinishing) {
                        productsSwipeRefreshLayout.isRefreshing = false
                        Toast.makeText(
                            this@MainActivity,
                            resources.getString(R.string.loading_products_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })


        compositeDisposable.add(diposable)
    }

    private fun requestPromotionalProducts(promotionalSingle: Single<List<Product>>): DisposableSingleObserver<List<Product>> {
        return promotionalSingle
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<Product>>() {
                override fun onSuccess(productList: List<Product>) {
                    if (!isFinishing) {
                        productsRecyclerAdapter.updatePromotionalList(productList.filter { it.promotional > 0 })
                    }
                }

                override fun onError(e: Throwable) {
                    if (!isFinishing) {
                        Toast.makeText(
                            this@MainActivity,
                            resources.getString(R.string.loading_promotional_products_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })
    }

    private fun requestProducts(productSingle: Single<List<Product>>): DisposableSingleObserver<List<Product>> {
        return productSingle
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<Product>>() {
                override fun onSuccess(productList: List<Product>) {
                    if (!isFinishing) {
                        productsRecyclerAdapter.productList = productList
                        productsRecyclerAdapter.notifyDataSetChanged()
                        productsSwipeRefreshLayout.isRefreshing = false
                    }
                }

                override fun onError(e: Throwable) {
                    if (!isFinishing) {
                        Toast.makeText(
                            this@MainActivity,
                            resources.getString(R.string.loading_products_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

}
