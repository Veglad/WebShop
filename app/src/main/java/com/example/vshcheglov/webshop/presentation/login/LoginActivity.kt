package com.example.vshcheglov.webshop.presentation.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.InputType
import android.widget.Toast
import androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON
import androidx.biometric.BiometricPrompt
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.extensions.isNetworkAvailable
import com.example.vshcheglov.webshop.presentation.helpres.MainThreadExecutor
import com.example.vshcheglov.webshop.presentation.main.MainActivity
import com.example.vshcheglov.webshop.presentation.registration.RegisterActivity
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_login.*
import nucleus5.factory.RequiresPresenter
import nucleus5.view.NucleusAppCompatActivity
import java.lang.Exception

@RequiresPresenter(LoginPresenter::class)
class LoginActivity : NucleusAppCompatActivity<LoginPresenter>(), LoginPresenter.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        orderButton.setOnClickListener {
            emailTextInput.error = ""
            passwordTextInput.error = ""
            presenter.logInUser(
                loginEmail.text.toString(),
                loginPassword.text.toString(), isNetworkAvailable()
            )
        }

        buttonRegister.setOnClickListener {
            presenter.registerUser()
        }
        useFingerprintButton.setOnClickListener {
            val biometricPrompt = BiometricPrompt(this, MainThreadExecutor(),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onFingerprintSuccess()
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.biometric_login_title))
                .setDescription(getString(R.string.biometric_login_description))
                .setNegativeButtonText(getString(R.string.cancel))
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                loginPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                loginPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    private fun onFingerprintSuccess() {
        Toast.makeText(this@LoginActivity, "success", Toast.LENGTH_LONG).show()
    }

    override fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
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
        Intent(this, RegisterActivity::class.java).apply {
            startActivity(this)
        }
    }

    override fun showNoInternetError() {
        Snackbar.make(
            loginConstraintLayout,
            resources.getString(R.string.no_internet_connection_warning),
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun showInvalidEmail() {
        emailTextInput.error = resources.getString(R.string.email_error)
    }

    override fun showInvalidPassword() {
        passwordTextInput.error = resources.getString(R.string.password_error)
    }

    override fun setShowProgress(isLoading: Boolean) {
        if (isLoading) {
            orderButton.startAnimation()
        } else {
            orderButton.revertAnimation()
        }
    }

    override fun onDestroy() {
        orderButton.dispose()
        super.onDestroy()
    }
}