package com.example.vshcheglov.webshop.presentation.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.example.vshcheglov.webshop.presentation.basket.BasketActivity
import com.example.vshcheglov.webshop.presentation.login.LoginActivity
import com.example.vshcheglov.webshop.presentation.main.adapters.ProductsRecyclerAdapter
import com.example.vshcheglov.webshop.presentation.main.adapters.SearchRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_error_layout.*
import kotlinx.android.synthetic.main.main_products.*
import kotlinx.android.synthetic.main.main_search_empty.*
import kotlinx.android.synthetic.main.main_search_list.*
import nucleus5.factory.RequiresPresenter
import nucleus5.view.NucleusAppCompatActivity
import timber.log.Timber

@RequiresPresenter(MainPresenter::class)
class MainActivity : NucleusAppCompatActivity<MainPresenter>(), MainPresenter.MainView {

    companion object {
        private const val SEARCH_KEY = "search"
    }

    private lateinit var searchView: SearchView
    private lateinit var headerUserEmail: TextView
    private var snackbar: Snackbar? = null
    private val productsRecyclerAdapter = ProductsRecyclerAdapter(this, mutableListOf(), mutableListOf())
    private val searchRecyclerAdapter = SearchRecyclerAdapter(this, mutableListOf())

    private lateinit var toggle: ActionBarDrawerToggle
    private var isErrorVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter?.loadProducts(isNetworkAvailable())

        headerUserEmail = mainNavigationView.getHeaderView(0).findViewById(R.id.navMainHeaderEmail)

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
        productsSwipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.primary),
            ContextCompat.getColor(this, R.color.color_accent),
            ContextCompat.getColor(this, R.color.dark_gray)
        )

        with(productsRecyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productsRecyclerAdapter
        }

        with(mainSearchRecyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = searchRecyclerAdapter
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

    override fun showProductList(productList: MutableList<Product>) {
        setErrorVisibility(false)
        productsRecyclerAdapter.setProductList(productList)
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
        if (isVisible) {
            showLayout(MainLayouts.ERROR)
        } else {
            showLayout(MainLayouts.PRODUCTS)
        }

        isErrorVisible = isVisible
        invalidateOptionsMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                mainDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.actionBasket -> {
                startActivity(Intent(this, BasketActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isErrorVisible) return false

        menu?.let {
            menuInflater.inflate(R.menu.main_menu, menu)
            val searchItem = menu.findItem(R.id.actionSearch)
            searchView = searchItem.actionView as SearchView
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.imeOptions = EditorInfo.IME_ACTION_DONE

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchText: String?): Boolean {
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(searchText: String?): Boolean {
                    if (searchText != null && searchText.isEmpty() || searchText == null) {
                        showLayout(MainLayouts.SEARCH_EMPTY)
                        mainSearchEmptyTextView.text = resources.getString(R.string.search_list_empty_query)
                    } else {
                        presenter.searchProducts(searchText)
                    }
                    return true
                }
            })

            searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    showLayout(MainLayouts.SEARCH_EMPTY)
                    mainSearchEmptyTextView.text = resources.getString(R.string.search_list_empty_query)
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    searchView.setQuery("", true)
                    showLayout(MainLayouts.PRODUCTS)
                    return true
                }
            })
        }

        return true
    }

    override fun showNoResults() {
        showLayout(MainLayouts.SEARCH_EMPTY)
        mainSearchEmptyTextView.text = resources.getString(R.string.no_search_result)
    }

    override fun showSearchedProducts(productList: List<Product>) {
        showLayout(MainLayouts.SEARCH_PRODUCTS)
        searchRecyclerAdapter.productList.clear()
        searchRecyclerAdapter.productList.addAll(productList)
        searchRecyclerAdapter.notifyDataSetChanged()
    }

    fun showLayout(mainLayouts: MainLayouts) {
        mainSearchEmptyLayout.visibility = View.GONE
        mainProductsLayout.visibility = View.GONE
        mainSearchListLayout.visibility = View.GONE
        mainErrorLayout.visibility = View.GONE

        when (mainLayouts) {
            MainLayouts.PRODUCTS -> mainProductsLayout.visibility = View.VISIBLE
            MainLayouts.SEARCH_PRODUCTS -> mainSearchListLayout.visibility = View.VISIBLE
            MainLayouts.SEARCH_EMPTY -> mainSearchEmptyLayout.visibility = View.VISIBLE
            MainLayouts.ERROR -> mainErrorLayout.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
        } else {
            super.onBackPressed()
        }
    }

    enum class MainLayouts {
        PRODUCTS, SEARCH_PRODUCTS, SEARCH_EMPTY, ERROR
    }
}
