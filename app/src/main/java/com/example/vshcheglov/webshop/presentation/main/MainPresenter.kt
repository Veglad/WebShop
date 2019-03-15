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
import nucleus5.presenter.RxPresenter
import timber.log.Timber
import javax.inject.Inject

class MainPresenter : RxPresenter<MainPresenter.MainView>() {
    @Inject lateinit var productRepository: ProductRepository

    private var isLoading = false
    private var isNetworkAvailable = false

    init {
        App.appComponent.inject(this)
    }

    fun loadProducts(isNetworkAvailable: Boolean) {
        this.isNetworkAvailable = isNetworkAvailable
        //if (isNetworkAvailable) {
            view?.setShowRetry(false)
            fetchProducts()
        //} else {
            //Timber.d("Internet is not available")
            //view?.setShowRetry(true)
        //}
    }

    private fun fetchProducts() {
        Timber.d("Fetching products...")
        isLoading = true
        view?.showLoading(true)

        val disposable = Single.zip(
            productRepository.getAllDevices(), productRepository.getAllPromotionalDevices()
            , BiFunction { products: List<Product>, promotionals: List<Product> ->
                Pair(products, promotionals)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Pair<List<Product>, List<Product>>>() {
                override fun onSuccess(pairProducts: Pair<List<Product>, List<Product>>) {
                    isLoading = false
                    Timber.d("Products fetched successfully")
                    val promotionalList = pairProducts.second.filter { it.percentageDiscount > 0 }
                    view?.let {
                        it.showLoading(false)
                        it.showProductList(pairProducts.first)
                        it.showPromotionalProductList(promotionalList)
                    }
                }

                override fun onError(e: Throwable) {
                    isLoading = false
                    Timber.e("Products fetching error:" + e)
                    view?.let {
                        it.showLoading(false)
                        it.showError(e)
                    }
                }
            })

        add(disposable)
    }

    override fun onTakeView(view: MainView?) {
        super.onTakeView(view)
        //view?.setShowRetry(!isNetworkAvailable)
        view?.showLoading(isLoading)
    }

    interface MainView {
        fun showLoading(isLoading: Boolean)

        fun setShowRetry(isVisible: Boolean)

        fun showError(throwable: Throwable)

        fun showProductList(productList: List<Product>)

        fun showPromotionalProductList(promotionalList: List<Product>)
    }
}