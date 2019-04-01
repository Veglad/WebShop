package com.example.vshcheglov.webshop.presentation.purchase

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.DataProvider
import com.example.vshcheglov.webshop.data.enteties.OrderProduct
import com.example.vshcheglov.webshop.data.users.UserRepository
import com.google.firebase.Timestamp
import nucleus5.presenter.Presenter
import javax.inject.Inject

class PurchasePresenter : Presenter<PurchasePresenter.View>() {

    @Inject
    lateinit var dataProvider: DataProvider

    init {
        App.appComponent.inject(this)
    }

    override fun onTakeView(view: View?) {
        super.onTakeView(view)
        dataProvider.getUserOrders { orderList ->
            if (orderList != null) {
                val productToTimeStampList = orderList.map { order ->
                    order.orderProducts.map { Pair(it, order.timestamp) }
                }.flatten()
                view?.showProducts(productToTimeStampList)
            } else {
                view?.showProductsFetchingError()
            }
        }
    }

    interface View {
        fun showProducts(productToTimeStampList: List<Pair<OrderProduct, Timestamp>>)

        fun showProductsFetchingError()
    }
}