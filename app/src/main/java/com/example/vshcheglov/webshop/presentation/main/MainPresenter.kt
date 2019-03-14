package com.example.vshcheglov.webshop.presentation.main

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.products.ProductRepository
import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class MainPresenter {
    @Inject lateinit var productRepository: ProductRepository
    private val compositeDisposable = CompositeDisposable()
    private var mainView: MainView? = null

    init {
        App.productsComponent.inject(this)
    }

    fun clearResources() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        Timber.d("Resources cleared")
    }

    fun loadProducts(isNetworkAvailable: Boolean) {
        Timber.d("Products load")
        if (isNetworkAvailable) {
            mainView?.setShowRetry(false)
            fetchProducts()
        } else {
            Timber.d("Internet is not available")
            mainView?.setShowRetry(true)
        }
    }

    private fun fetchProducts() {
        Timber.d("Fetching products...")
        mainView?.showLoading()

        val disposable = Single.zip(
            productRepository.getAllDevices(), productRepository.getAllPromotionalDevices()
            , BiFunction { products: List<Product>, promotionals: List<Product> ->
                Pair(products, promotionals)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Pair<List<Product>, List<Product>>>() {
                override fun onSuccess(pairProducts: Pair<List<Product>, List<Product>>) {
                    Timber.d("Products fetched successfully")
                    val promotionalList = pairProducts.second.filter { it.percentageDiscount > 0 }
                    mainView?.let {
                        it.hideLoading()
                        it.showProductList(pairProducts.first)
                        it.showPromotionalProductList(promotionalList)
                    }
                }

                override fun onError(e: Throwable) {
                    Timber.e("Products fetching error:" + e)
                    mainView?.let {
                        it.hideLoading()
                        it.showError(e)
                    }
                }
            })

        compositeDisposable.add(disposable)
    }

    fun onAttached(mainView: MainView) {
        this.mainView = mainView
    }

    fun onDetached() {
        mainView = null
    }

    interface MainView {
        fun showLoading()

        fun hideLoading()

        fun setShowRetry(isVisible: Boolean)

        fun showError(throwable: Throwable)

        fun showProductList(productList: List<Product>)

        fun showPromotionalProductList(promotionalList: List<Product>)
    }
}