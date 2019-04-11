package com.example.vshcheglov.webshop.presentation.login

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.DataProvider
import com.example.vshcheglov.webshop.extensions.isEmailValid
import com.example.vshcheglov.webshop.extensions.isPasswordValid
import com.example.vshcheglov.webshop.presentation.helpres.Encryptor
import kotlinx.coroutines.*
import nucleus5.presenter.Presenter
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class LoginPresenter : Presenter<LoginPresenter.View>() {

    @Inject
    lateinit var dataProvider: DataProvider
    @Inject
    lateinit var encryptor: Encryptor

    private val job = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + job)

    init {
        App.appComponent.inject(this)
    }

    override fun onTakeView(view: View?) {
        super.onTakeView(view)
        if (dataProvider.isSignedIn) {
            Timber.d("user is authorized")
            view?.startMainActivity()
        }
    }

    fun logInUser(email: String, password: String, isNetworkAvailable: Boolean) {
        var isValid = true
        if (!email.isEmailValid()) {
            view?.showInvalidEmail()
            isValid = false
        }
        if (!password.isPasswordValid()) {
            view?.showInvalidPassword()
            isValid = false
        }

        if (isNetworkAvailable) {
            if (!isValid) return
            performLogin(email, password)
        } else {
            view?.showNoInternetError()
        }
    }

    private fun performLogin(email: String, password: String) {
        uiCoroutineScope.launch {
            view?.setShowProgress(true)
            try {
                dataProvider.signInUser(email, password)

                if (!dataProvider.containsUserCredentials()) {
                    val encryptedPassword = encryptor.encode(password)
                    encryptedPassword?.let {
                        dataProvider.saveUserCredentialsLocal(email, encryptedPassword)
                    }
                }

                view?.startMainActivity()
            } catch (ex: Exception) {
                Timber.e("user sign in error: $ex")
                view?.showLoginError(ex)
            } finally {
                view?.setShowProgress(false)
            }
        }
    }

    fun registerUser() {
        view?.startRegisterActivity()
    }

    override fun onDropView() {
        super.onDropView()
        job.cancel()
    }

    interface View {
        fun startMainActivity()

        fun showLoginError(exception: Exception?)

        fun startRegisterActivity()

        fun showNoInternetError()

        fun showInvalidEmail()

        fun showInvalidPassword()

        fun setShowProgress(isLoading: Boolean)
    }
}