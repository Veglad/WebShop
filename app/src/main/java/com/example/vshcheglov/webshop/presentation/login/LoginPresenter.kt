package com.example.vshcheglov.webshop.presentation.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import nucleus5.presenter.Presenter

class LoginPresenter : Presenter<LoginPresenter.PresenterView>() {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser: FirebaseUser? = firebaseAuth.currentUser


    override fun onTakeView(view: PresenterView?) {
        super.onTakeView(view)
        if (currentUser != null) {
            view?.startMainActivity()
        }
    }

    interface PresenterView {
        fun startMainActivity()
    }
}