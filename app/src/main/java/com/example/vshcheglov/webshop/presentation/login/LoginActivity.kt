package com.example.vshcheglov.webshop.presentation.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.presentation.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import nucleus5.factory.RequiresPresenter
import nucleus5.view.NucleusAppCompatActivity
import java.lang.Exception

@RequiresPresenter(LoginPresenter::class)
class LoginActivity : NucleusAppCompatActivity<LoginPresenter>(), LoginPresenter.PresenterView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        buttonLogin.setOnClickListener {
            presenter.logOnUser(loginEmail.text.toString(), loginPassword.text.toString())
        }

        buttonRegister.setOnClickListener {
            presenter.registerUser()
        }
    }

    override fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun showLoginError(exception: Exception?) {
        Toast.makeText(
            this, "Authentication failed: $exception",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun startRegisterActivity() {
        //val intent = Intent(this, MainActivity::class.java)
        //startActivity(intent)
    }

    override fun showNoInternetError() {
        Snackbar.make(
            loginConstraintLayout,
            resources.getString(R.string.no_internet_connection_warning),
            Snackbar.LENGTH_LONG
        ).show()
    }
}
