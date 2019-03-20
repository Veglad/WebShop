package com.example.vshcheglov.webshop.presentation.main

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.AllProducts
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
    @Inject lateinit var productRepository: ProductRepository

    private var isLoading = false
    private var isNetworkAvailable = false

    private val job = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + job)

    init {
        App.appComponent.inject(this)
    }

    fun loadProducts(isNetworkAvailable: Boolean) {
        this.isNetworkAvailable = isNetworkAvailable
        if (!isNetworkAvailable) {
            view?.showNoInternetWarning()
        }
        fetchProducts()
    }


    private fun fetchProducts() {
        uiCoroutineScope.launch {
            Timber.d("Fetching products...")
            isLoading = true
            view?.showLoading(true)

            try {
               val allProducts = productRepository.getAllProducts()
                processUiWithAllProducts(allProducts)
            } catch (ex: Exception) {
                Timber.e("Products fetching error:" + ex)
                view?.let {
                    it.showError(ex)
                    it.showNoInternetWarning()
                }
            } finally {
                view?.showLoading(false)
            }
        }
    }

    private fun processUiWithAllProducts(allProducts: AllProducts) {
        Timber.d("Products fetched successfully")
        val promotionalList = allProducts.promotionalProducts.filter { it.percentageDiscount > 0 }
        view?.let {
            it.showProductList(allProducts.products)
            it.showPromotionalProductList(promotionalList)
        }
    }

    override fun onTakeView(view: MainView?) {
        super.onTakeView(view)
        if (!isNetworkAvailable) {
            view?.showNoInternetWarning()
        }
        view?.showLoading(isLoading)
    }

    interface MainView {
        fun showLoading(isLoading: Boolean)

        fun showNoInternetWarning()

        fun showError(throwable: Throwable)

        fun showProductList(productList: List<Product>)

        fun showPromotionalProductList(promotionalList: List<Product>)
    }
}