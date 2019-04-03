package com.example.vshcheglov.webshop.presentation.purchase

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.DataProvider
import com.example.vshcheglov.webshop.domain.OrderProduct
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nucleus5.presenter.Presenter
import javax.inject.Inject

class PurchasePresenter : Presenter<PurchasePresenter.View>() {

    @Inject
    lateinit var dataProvider: DataProvider

    private val job = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + job)

    init {
        App.appComponent.inject(this)
    }

    override fun onTakeView(view: View?) {
        super.onTakeView(view)
        uiCoroutineScope.launch {
            try {
                view?.setShowLoading(true)
                val orderList = dataProvider.getUserOrders()
                if (orderList.isNotEmpty()) {//TODO: Process empty list
                    val productToTimeStampList = orderList.map { order ->
                        order.orderProducts.map { Pair(it, order.timestamp) }
                    }.flatten()
                    view?.showProducts(productToTimeStampList)
                } else {
                    view?.showNoData()
                }
            } catch (ex:Exception) {
                view?.showProductsFetchingError(ex)
            } finally {
                view?.setShowLoading(false)
            }
        }
    }

    override fun onDropView() {
        super.onDropView()
        job.cancel()
    }

    interface View {
        fun showProducts(productToTimeStampList: List<Pair<OrderProduct, Timestamp>>)

        fun showProductsFetchingError(exception: Exception)

        fun showNoData()

        fun setShowLoading(isLoading: Boolean)
    }
}