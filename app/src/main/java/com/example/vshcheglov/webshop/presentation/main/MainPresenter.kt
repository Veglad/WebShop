package com.example.vshcheglov.webshop.presentation.main

import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.data.ProductRepository
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainPresenter(val mainView: IMainView) {

    private val compositeDisposable = CompositeDisposable()

    fun onCreate() {
        loadProductsIfInternetAvailable()
    }

    fun tryAgainButtonClick() {
        loadProductsIfInternetAvailable()
    }

    fun onRefresh() {
        fetchProducts()
    }

    fun onDestroy() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    private fun loadProductsIfInternetAvailable() {
        if (mainView.context.isNetworkAvailable()) {
            mainView.hideRetry()
            fetchProducts()
        } else {
            mainView.showRetry()
        }
    }

    private fun fetchProducts() {
        mainView.showLoading()

        val disposable = Single.zip(
            ProductRepository.getAllDevices(), ProductRepository.getAllPromotionalDevices()
            , BiFunction { products: List<Product>, promotionals: List<Product> ->
                Pair(products, promotionals)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Pair<List<Product>, List<Product>>>() {
                override fun onSuccess(pairProducts: Pair<List<Product>, List<Product>>) {
                    mainView.hideLoading()
                    val promotionalList = pairProducts.second.filter { it.percentageDiscount > 0 }

                    mainView.showProductList(pairProducts.first)
                    mainView.showPromotionalProductList(promotionalList)
                }

                override fun onError(e: Throwable) {
                    mainView.hideLoading()
                    mainView.showError(mainView.context.resources.getString(R.string.loading_products_error))
                }
            })

        compositeDisposable.add(disposable)
    }
}