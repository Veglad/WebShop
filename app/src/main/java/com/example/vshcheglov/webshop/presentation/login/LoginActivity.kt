package com.example.vshcheglov.webshop.presentation.login

import android.os.Bundle
import com.example.vshcheglov.webshop.R
import nucleus5.factory.RequiresPresenter
import nucleus5.view.NucleusAppCompatActivity

@RequiresPresenter(LoginPresenter::class)
class LoginActivity : NucleusAppCompatActivity<LoginPresenter>(), LoginPresenter.PresenterView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter.checkIfUserAuthorized()
    }
}
