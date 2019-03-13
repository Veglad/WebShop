package com.example.vshcheglov.webshop.presentation.main

import com.example.vshcheglov.webshop.data.products.ProductRepository
import com.example.vshcheglov.webshop.domain.Product
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainPresenter(private var mainView: MainView?, private val productRepository: ProductRepository) {

    private var compositeDisposable = CompositeDisposable()

    fun clearResources() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    fun loadProducts(isNetworkAvailable: Boolean) {
        if (isNetworkAvailable) {
            mainView?.setShowRetry(false)
            fetchProducts()
        } else {
            mainView?.setShowRetry(true)
        }
    }

    private fun fetchProducts() {

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
                    val promotionalList = pairProducts.second.filter { it.percentageDiscount > 0 }
                    mainView?.let {
                        it.hideLoading()
                        it.showProductList(pairProducts.first)
                        it.showPromotionalProductList(promotionalList)
                    }
                }

                override fun onError(e: Throwable) {
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