package com.example.vshcheglov.webshop.presentation.bought

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.users.UserRepository
import nucleus5.presenter.Presenter
import javax.inject.Inject

class BoughtPresenter : Presenter<BoughtPresenter.View>() {

    @Inject
    lateinit var userRepository: UserRepository

    init {
        App.appComponent.inject(this)
    }

    override fun onTakeView(view: View?) {
        super.onTakeView(view)
        userRepository.getUserOrders {orderList ->
            if(orderList != null) {

            } else {

            }
        }
    }

    interface View {

    }
}