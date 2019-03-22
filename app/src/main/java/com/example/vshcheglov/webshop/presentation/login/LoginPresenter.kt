package com.example.vshcheglov.webshop.presentation.login

import android.content.Context
import android.util.Patterns
import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import nucleus5.presenter.Presenter
import timber.log.Timber
import java.lang.Exception
import java.util.regex.Pattern
import javax.inject.Inject

class LoginPresenter : Presenter<LoginPresenter.PresenterView>() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var appContext: Context
    private var currentUser: FirebaseUser? = null

    init {
        App.appComponent.inject(this)
        currentUser = firebaseAuth.currentUser
    }


    override fun onTakeView(view: PresenterView?) {
        super.onTakeView(view)
        if (currentUser != null) {
            Timber.d("user is authorized")
            view?.startMainActivity()
        }
    }

    fun logOnUser(email: String, password: String) {
        var isValid = true
        if (!isEmailValid(email)) {
            view?.showInvalidEmail()
            isValid = false
        }
        if (!isPasswordValid(password)) {
            view?.showInvalidPassword()
            isValid = false
        }

        if (appContext.isNetworkAvailable()) {
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
                    view?.showLogInSuccess()
                    view?.startMainActivity()
                    view?.setShowProgress(false)
                } else {
                    Timber.e("user sign in error: " + task.exception)
                    view?.showLoginError(task.exception)
                    view?.setShowProgress(false)
                }
            }
    }

    private fun isEmailValid(email: String) =
        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordValid(password: String) =
        password.length >= 6 && password.isNotEmpty()

    fun registerUser() {
        view?.startRegisterActivity()
    }

    interface PresenterView {
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