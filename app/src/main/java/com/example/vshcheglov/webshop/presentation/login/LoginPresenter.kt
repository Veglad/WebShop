package com.example.vshcheglov.webshop.presentation.login

import android.content.Context
import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import nucleus5.presenter.Presenter
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class LoginPresenter : Presenter<LoginPresenter.PresenterView>() {

    @Inject lateinit var firebaseAuth: FirebaseAuth
    @Inject lateinit var appContext: Context
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
        if (appContext.isNetworkAvailable()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.d("user sign in success")
                        view?.showLogInSuccess()
                        view?.startMainActivity()
                    } else {
                        Timber.e("user sign in error: " + task.exception)
                        view?.showLoginError(task.exception)
                    }
                }
        } else {
            view?.showNoInternetError()
        }


    }

    fun registerUser() {
        view?.startRegisterActivity()
    }

    interface PresenterView {
        fun startMainActivity()

        fun showLoginError(exception: Exception?)

        fun startRegisterActivity()

        fun showNoInternetError()

        fun showLogInSuccess()
    }
}