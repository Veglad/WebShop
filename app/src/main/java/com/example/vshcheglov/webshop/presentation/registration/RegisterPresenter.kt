package com.example.vshcheglov.webshop.presentation.registration

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.users.UserRepository
import com.example.vshcheglov.webshop.extensions.isEmailValid
import com.example.vshcheglov.webshop.extensions.isPasswordValid
import nucleus5.presenter.Presenter
import timber.log.Timber
import javax.inject.Inject

class RegisterPresenter : Presenter<RegisterPresenter.View>() {
    @Inject
    lateinit var userRepository: UserRepository

    init {
        App.appComponent.inject(this)
    }

    fun registerUser(email: String, password: String, confirmPassword: String, isNetworkAvailable: Boolean) {
        var isValid = true

        view?.let {
            if (!email.isEmailValid()) {
                it.showInvalidEmail()
                isValid = false
            }
            if (password != confirmPassword) {
                it.showPasswordsNotMatchError()
                isValid = false
            }
            if (!password.isPasswordValid()) {
                it.showInvalidPassword()
                isValid = false
            }
            if (!confirmPassword.isPasswordValid()) {
                it.showInvalidConfirmPassword()
                isValid = false
            }

            if (isNetworkAvailable) {
                if (!isValid) return
                view?.setShowProgress(true)
                registerUserWithEmailAndPassword(email, password)
            } else {
                it.showNoInternetError()
            }
        }
    }

    private fun registerUserWithEmailAndPassword(email: String, password: String) {
        view?.setShowProgress(true)
        userRepository.registerUserWithEmailAndPassword(email, password) { task ->
            if (task.isSuccessful) {
                Timber.d("user registration success")
                view?.showLogInSuccess()
                view?.setShowProgress(false)
                view?.startMainActivity()
            } else {
                Timber.e("user registration error: " + task.exception)
                view?.showLoginError(task.exception)
                view?.setShowProgress(false)
            }
        }
    }

    interface View {
        fun showInvalidEmail()

        fun showInvalidPassword()

        fun showNoInternetError()

        fun setShowProgress(isLoading: Boolean)

        fun showLogInSuccess()

        fun showLoginError(exception: Exception?)

        fun startMainActivity()

        fun showInvalidConfirmPassword()

        fun showPasswordsNotMatchError()
    }
}