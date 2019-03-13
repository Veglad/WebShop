package com.example.vshcheglov.webshop.presentation.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
import com.example.vshcheglov.webshop.data.network.WebShopApi
import com.example.vshcheglov.webshop.data.products.NetworkDataSource
import com.example.vshcheglov.webshop.data.products.ProductRepository
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.example.vshcheglov.webshop.presentation.main.adapters.ProductsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main_primary.*
import kotlinx.android.synthetic.main.activity_main_error_layout.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity(), MainPresenter.MainView {

    private lateinit var productsRecyclerAdapter: ProductsRecyclerAdapter

    private val interceptor = HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://us-central1-webshop-58013.cloudfunctions.net")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private val mainPresenter = MainPresenter(
        this,
        ProductRepository(
            NetworkDataSource(
                ProductEntityDataMapper(),
                retrofit.create(WebShopApi::class.java)
            )
        )
    )

    override fun onAttachedToWindow() {
        mainPresenter.onAttached(this)
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        mainPresenter.onDetached()
        super.onDetachedFromWindow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainPresenter.loadProducts(isNetworkAvailable())

        tryAgainButton.setOnClickListener {
            mainPresenter.loadProducts(isNetworkAvailable())
        }
        productsSwipeRefreshLayout.setOnRefreshListener {
            mainPresenter.loadProducts(isNetworkAvailable())
        }

        productsRecyclerAdapter = ProductsRecyclerAdapter(this, mutableListOf(), mutableListOf())
        with(productsRecyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productsRecyclerAdapter
        }
    }

    override fun onDestroy() {
        mainPresenter.clearResources()
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
