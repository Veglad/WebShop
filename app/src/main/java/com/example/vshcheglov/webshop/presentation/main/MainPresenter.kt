package com.example.vshcheglov.webshop.presentation.main

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.AllProductsEntity
import com.example.vshcheglov.webshop.data.products.ProductRepository
import com.example.vshcheglov.webshop.domain.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nucleus5.presenter.Presenter
import timber.log.Timber
import javax.inject.Inject

class MainPresenter : Presenter<MainPresenter.MainView>() {
    @Inject
    lateinit var productRepository: ProductRepository

    private var isLoading = false
    private var isNetworkAvailable = false

    private val job = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + job)

    private var allProducts: AllProductsEntity? = null

    init {
        App.appComponent.inject(this)
    }

    fun loadProducts(isNetworkAvailable: Boolean) {
        this.isNetworkAvailable = isNetworkAvailable
        fetchProducts(true, isNetworkAvailable)
    }


    private fun fetchProducts(refresh: Boolean, isNetworkAvailable: Boolean) {
        uiCoroutineScope.launch {
            Timber.d("Fetching products...")

            try {
                if (allProducts == null || refresh) {
                    isLoading = true
                    view?.showLoading(true)
                    allProducts = productRepository.getAllProducts()
                }

                processUiWithAllProducts(allProducts!!)

                if (!isNetworkAvailable) {
                    view?.showNoInternetWarning()
                }
            } catch (ex: Exception) {
                Timber.e("Products fetching error:" + ex)
                view?.let {
                    it.showError(ex)
                }
            } finally {
                isLoading = false
                view?.showLoading(false)
            }
        }
    }

    private fun processUiWithAllProducts(allProducts: AllProductsEntity) {
        Timber.d("Products fetched successfully")
        val promotionalList = allProducts.promotionalProducts.filter { it.percentageDiscount > 0 }
        view?.let {
            it.showProductList(allProducts.products)
            it.showPromotionalProductList(promotionalList)
        }
    }

    override fun onTakeView(view: MainView?) {
        super.onTakeView(view)
        view?.showLoading(isLoading)
        fetchProducts(false, isNetworkAvailable)
    }

    interface MainView {
        fun showLoading(isLoading: Boolean)

        fun showNoInternetWarning()

        fun showError(throwable: Throwable)

        fun showProductList(productList: List<Product>)

        fun showPromotionalProductList(promotionalList: List<Product>)
    }
}