package com.example.vshcheglov.webshop.presentation.login

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.extensions.isEmailValid
import com.example.vshcheglov.webshop.extensions.isPasswordValid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import nucleus5.presenter.Presenter
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class LoginPresenter : Presenter<LoginPresenter.View>() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    init {
        App.appComponent.inject(this)
        currentUser = firebaseAuth.currentUser
    }


    override fun onTakeView(view: View?) {
        super.onTakeView(view)
        if (currentUser != null) {
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
            signInUser(email, password)
        } else {
            view?.showNoInternetError()
        }
    }

    private fun signInUser(email: String, password: String) {
        view?.setShowProgress(true)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("user sign in success")
                    view?.let {
                        it.showLogInSuccess()
                        it.startMainActivity()
                        it.setShowProgress(false)
                    }
                } else {
                    Timber.e("user sign in error: " + task.exception)
                    view?.let {
                        it.showLoginError(task.exception)
                        it.setShowProgress(false)
                    }
                }
            }
    }

    fun registerUser() {
        view?.startRegisterActivity()
    }

    interface View {
        fun startMainActivity()

        fun showLoginError(exception: Exception?)

        fun startRegisterActivity()

        fun showNoInternetError()

        fun showLogInSuccess()

        fun showInvalidEmail()

        fun showInvalidPassword()

        fun setShowProgress(isLoading: Boolean)
    }
}