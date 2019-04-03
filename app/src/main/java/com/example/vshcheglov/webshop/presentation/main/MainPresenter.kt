package com.example.vshcheglov.webshop.presentation.main

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.DataProvider
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.main.helpers.SearchFilter
import kotlinx.coroutines.*
import nucleus5.presenter.Presenter
import timber.log.Timber
import javax.inject.Inject

class MainPresenter : Presenter<MainPresenter.MainView>() {
    @Inject
    lateinit var dataProvider: DataProvider

    private var isLoading = false
    private var isNetworkAvailable = false

    private val job = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + job)

    private var productList: MutableList<Product>? = null
    private var promotionalProductList: MutableList<Product>? = null

    private lateinit var searchFilter: SearchFilter

    init {
        App.appComponent.inject(this)
    }

    fun loadProducts(isNetworkAvailable: Boolean) {
        this.isNetworkAvailable = isNetworkAvailable
        fetchProducts(true, isNetworkAvailable)
    }

    override fun onDropView() {
        super.onDropView()
        job.cancel()
    }

    private fun fetchProducts(refresh: Boolean, isNetworkAvailable: Boolean) {
        uiCoroutineScope.launch {
            Timber.d("Fetching products...")

            try {
                if (productList == null || promotionalProductList == null || refresh) {
                    isLoading = true
                    view?.showLoading(true)

                    val productsDeferred = uiCoroutineScope.async { dataProvider.getProducts() }
                    val promotionalProductsDeferred = uiCoroutineScope.async { dataProvider.getPromotionalProducts() }
                    productList = productsDeferred.await()
                    promotionalProductList = promotionalProductsDeferred.await()
                }

                productList?.let { products ->
                    promotionalProductList?.let { promotionalProducts ->
                        processUiWithProducts(products, promotionalProducts)
                    }
                }

                if (!isNetworkAvailable) {
                    view?.showNoInternetWarning()
                }
            } catch (ex: Exception) {
                Timber.e("Products fetching error:$ex")
                view?.let {
                    it.showError(ex)
                }
            } finally {
                isLoading = false
                view?.showLoading(false)
            }
        }
    }

    private fun processUiWithProducts(
        productList: MutableList<Product>,
        promotionalProductList: MutableList<Product>
    ) {
        Timber.d("Products fetched successfully")
        view?.let {
            it.showProductList(productList)
            it.showPromotionalProductList(promotionalProductList)
        }
    }

    private fun showUserEmail() {
        uiCoroutineScope.launch {
            try {
                val user = withContext(Dispatchers.IO) { dataProvider.getCurrentUser() }
                view?.showUserEmail(user.email)
            } catch (ex: Exception) {
                view?.showEmailLoadError(ex)
            }
        }

    }

    override fun onTakeView(view: MainView?) {
        super.onTakeView(view)
        view?.showLoading(isLoading)
        fetchProducts(false, isNetworkAvailable)
        showUserEmail()
    }

    fun logOut() {
        uiCoroutineScope.launch {
            withContext(Dispatchers.IO) { dataProvider.logOut() }
            view?.startLoginActivity()
        }
    }

    fun searchProducts(searchText: String) {
        view?.showLoading(true)
        productList?.let {
            val searchFilter = SearchFilter(it) { productList: List<Product>? ->
                view?.showLoading(false)
                if (productList == null || productList.isEmpty()) {
                    view?.showNoResults()
                } else {
                    view?.showSearchedProducts(productList)
                }
            }
            searchFilter.filter.filter(searchText)
        }
    }

    interface MainView {
        fun showLoading(isLoading: Boolean)

        fun showNoInternetWarning()

        fun showError(throwable: Throwable)

        fun showEmailLoadError(throwable: Throwable)

        fun showProductList(productList: MutableList<Product>)

        fun showPromotionalProductList(promotionalList: List<Product>)

        fun startLoginActivity()

        fun showUserEmail(email: String?)

        fun showNoResults()

        fun showSearchedProducts(productList: List<Product>)
    }
}