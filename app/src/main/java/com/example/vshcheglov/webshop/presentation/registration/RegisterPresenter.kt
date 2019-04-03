package com.example.vshcheglov.webshop.presentation.registration

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.DataProvider
import com.example.vshcheglov.webshop.data.users.UserRepository
import com.example.vshcheglov.webshop.extensions.isEmailValid
import com.example.vshcheglov.webshop.extensions.isPasswordValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nucleus5.presenter.Presenter
import timber.log.Timber
import javax.inject.Inject

class RegisterPresenter : Presenter<RegisterPresenter.View>() {
    @Inject
    lateinit var dataProvider: DataProvider

    private val job = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + job)

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
        uiCoroutineScope.launch {
            view?.setShowProgress(true)
            try {
                dataProvider.registerUser(email, password)
                Timber.d("user registration success")
                view?.let {
                    it.setShowProgress(false)
                    it.startMainActivity()
                }
            } catch (ex: Exception) {
                Timber.e("user registration error: $ex")
                view?.showLoginError(ex)
            } finally {
                view?.setShowProgress(false)
            }
        }
    }

    override fun onDropView() {
        super.onDropView()
        job.cancel()
    }

    interface View {
        fun showInvalidEmail()

        fun showInvalidPassword()

        fun showNoInternetError()

        fun setShowProgress(isLoading: Boolean)
        
        fun showLoginError(exception: Exception?)

        fun startMainActivity()

        fun showInvalidConfirmPassword()

        fun showPasswordsNotMatchError()
    }
}