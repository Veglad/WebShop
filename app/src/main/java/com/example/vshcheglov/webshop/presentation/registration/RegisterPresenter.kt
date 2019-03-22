package com.example.vshcheglov.webshop.presentation.registration

import android.content.Context
import android.util.Patterns
import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import nucleus5.presenter.Presenter
import timber.log.Timber
import javax.inject.Inject

class RegisterPresenter : Presenter<RegisterPresenter.RegisterView>() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var appContext: Context
    private var currentUser: FirebaseUser? = null

    init {
        App.appComponent.inject(this)
    }

    fun registerUser(email: String, password: String) {
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
            registerUserWithEmailAndPassword(email, password)
        } else {
            view?.showNoInternetError()
        }
    }

    private fun registerUserWithEmailAndPassword(email: String, password: String) {
        view?.setShowProgress(true)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
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

    private fun isEmailValid(email: String) =
        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordValid(password: String) =
        password.length >= 6 && password.isNotEmpty()

    interface RegisterView {
        fun showInvalidEmail()

        fun showInvalidPassword()

        fun showNoInternetError()

        fun setShowProgress(isLoading: Boolean)

        fun showLogInSuccess()

        fun showLoginError(exception: Exception?)

        fun startMainActivity()
    }
}