package com.example.vshcheglov.webshop.presentation.login

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.presentation.main.MainActivity
import com.example.vshcheglov.webshop.presentation.registration.RegisterActivity
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
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
            emailTextInput.error = ""
            passwordTextInput.error = ""
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
        val errorMessage = when (exception) {
            is FirebaseAuthInvalidUserException -> {
                resources.getString(R.string.incorrect_email_for_user)
            }
            is FirebaseAuthInvalidCredentialsException -> {
                resources.getString(R.string.incorrect_password_for_user)
            }
            else -> {
                resources.getString(R.string.unknown_error)
            }
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun showNoInternetError() {
        Snackbar.make(
            loginConstraintLayout,
            resources.getString(R.string.no_internet_connection_warning),
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun showLogInSuccess() {
        Toast.makeText(this, "Successfully authorized!", Toast.LENGTH_LONG).show()
    }

    override fun showInvalidEmail() {
        emailTextInput.error = resources.getString(R.string.email_error)
    }

    override fun showInvalidPassword() {
        passwordTextInput.error = resources.getString(R.string.password_error)
    }

    override fun setShowProgress(isLoading: Boolean) {
        if(isLoading) {
            buttonLogin.startAnimation()
        } else {
            buttonLogin.revertAnimation()
        }
    }

    override fun onDestroy() {
        buttonLogin.dispose()
        super.onDestroy()
    }
}
