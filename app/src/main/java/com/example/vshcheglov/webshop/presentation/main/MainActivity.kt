package com.example.vshcheglov.webshop.presentation.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.example.vshcheglov.webshop.presentation.basket.BasketActivity
import com.example.vshcheglov.webshop.presentation.login.LoginActivity
import com.example.vshcheglov.webshop.presentation.main.adapters.ProductsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_error_layout.*
import kotlinx.android.synthetic.main.activity_main_primary.*
import nucleus5.factory.RequiresPresenter
import nucleus5.view.NucleusAppCompatActivity
import timber.log.Timber

@RequiresPresenter(MainPresenter::class)
class MainActivity : NucleusAppCompatActivity<MainPresenter>(), MainPresenter.MainView {

    private lateinit var headerUserEmail: TextView
    private var snackbar: Snackbar? = null
    private lateinit var productsRecyclerAdapter: ProductsRecyclerAdapter
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter?.loadProducts(isNetworkAvailable())

        headerUserEmail = mainNavigationView.getHeaderView(0)
            .findViewById(R.id.navMainHeaderEmail)

        tryAgainButton.setOnClickListener {
            val isNetworkAvailable = isNetworkAvailable()

            setErrorVisibility(!isNetworkAvailable)
            presenter?.loadProducts(isNetworkAvailable)
            if (isNetworkAvailable) {
                snackbar?.dismiss()
            }
        }
        productsSwipeRefreshLayout.setOnRefreshListener {
            val isNetworkAvailable = isNetworkAvailable()

            Timber.d("Refresh data triggered")
            presenter?.loadProducts(isNetworkAvailable)
            if (isNetworkAvailable) {
                snackbar?.dismiss()
            }
        }

        productsRecyclerAdapter = ProductsRecyclerAdapter(this, mutableListOf(), mutableListOf())
        with(productsRecyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productsRecyclerAdapter
        }

        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initNavigationDrawer()
    }

    private fun initNavigationDrawer() {
        mainNavigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_main_log_out -> presenter.logOut()
                R.id.nav_main_basket -> presenter.showBasket()
            }

            mainDrawerLayout.closeDrawers()
            true
        }

        toggle = ActionBarDrawerToggle(this, mainDrawerLayout, R.string.open, R.string.close)
        mainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
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
        snackbar = Snackbar.make(
            mainFrameLayout,
            getString(R.string.no_internet_connection_warning), Snackbar.LENGTH_INDEFINITE
        )
        snackbar?.setAction(getString(R.string.try_again_button)) {
            if (isNetworkAvailable) {
                setErrorVisibility(false)
            }

            presenter?.loadProducts(isNetworkAvailable)
            snackbar?.dismiss()
        }
        snackbar?.show()
    }

    override fun showError(throwable: Throwable) {
        setErrorVisibility(true)
    }

    override fun showProductList(productList: List<Product>) {
        setErrorVisibility(false)
        productsRecyclerAdapter.productList = productList
        productsRecyclerAdapter.notifyDataSetChanged()
    }

    override fun showPromotionalProductList(promotionalList: List<Product>) {
        setErrorVisibility(false)
        productsRecyclerAdapter.updatePromotionalList(promotionalList)
    }

    override fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun startBasketActivity() {
        startActivity(Intent(this, BasketActivity::class.java))
    }

    override fun showUserEmail(email: String?) {
        email?.let {
            headerUserEmail.text = it
        }
    }

    private fun setErrorVisibility(isVisible: Boolean) {
        activityMainErrorLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
        activityMainPrimaryLayout.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                mainDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
