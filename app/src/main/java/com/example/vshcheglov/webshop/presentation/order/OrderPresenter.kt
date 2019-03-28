package com.example.vshcheglov.webshop.presentation.order

import com.example.vshcheglov.webshop.data.users.UserRepository
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.extensions.isCardNumberValid
import com.example.vshcheglov.webshop.extensions.isCvValid
import nucleus5.presenter.Presenter
import java.util.*
import javax.inject.Inject

class OrderPresenter : Presenter<OrderPresenter.OrderView>() {

    companion object {
        const val MIN_NAME_LENGTH = 2
        const val MIN_CARD_MONTH_NUMBER = 1
        const val MAX_CARD_MONTH_NUMBER = 12
        const val MAX_CARD_YEAR_NUMBER = 30
    }

    @Inject
    lateinit var userRepository: UserRepository

    fun initOrderPrice() {
        val orderPrice = Basket.totalPriceWithDiscount
        view?.setOrderPrice(orderPrice)
    }

    fun makeOrder(name: String, lastName: String, cardNumber: String,
                  cardMonth: Int?, cardYear: Int?, cardCv: String, isNetworkAvailable: Boolean) {
        var isValid = true
        view?.let {
            it.setShowProgress(true)

            if (name.length < MIN_NAME_LENGTH) {
                isValid = false
                it.showInvalidName()
            }
            if (lastName.length < MIN_NAME_LENGTH) {
                isValid = false
                it.showInvalidSecondName()
            }
            if (!cardNumber.isCardNumberValid()) {
                isValid = false
                it.showInvalidCardNumber()
            }
            if (cardMonth == null || cardMonth !in MIN_CARD_MONTH_NUMBER..MAX_CARD_MONTH_NUMBER) {
                isValid = false
                it.showInvalidCardMonth()
            }
            if (cardYear == null ||
                cardYear !in Calendar.getInstance().get(Calendar.YEAR) % 100..MAX_CARD_YEAR_NUMBER) {
                isValid = false
                it.showInvalidCardYear()
            }
            if (!cardCv.isCvValid()) {
                isValid = false
                it.showInvalidCardCv()
            }

            if (isNetworkAvailable) {
                if (isValid) {
                    it.notifyOrderCompleted()
                    it.setShowProgress(false)
                } else {
                    it.setShowProgress(false)
                }
            } else {
                it.showNoInternetError()
                it.setShowProgress(false)
            }
        }
    }

    interface OrderView {
        fun setOrderPrice(orderPrice: Double)

        fun setShowProgress(isVisible: Boolean)

        fun showInvalidName()

        fun showInvalidSecondName()

        fun showInvalidCardNumber()

        fun showInvalidCardMonth()

        fun showInvalidCardYear()

        fun showInvalidCardCv()

        fun showNoInternetError()

        fun notifyOrderCompleted()
    }
}