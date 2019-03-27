package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber
import javax.inject.Inject

class UserRepository : UserStorage {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase

    init {
        App.appComponent.inject(this)
    }

    override fun registerUserWithEmailAndPassword(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit
    ) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                completeCallback(task)
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        val userReference = firebaseDatabase.getReference("users").child(user.uid)
                        userReference.setValue(User(user.uid, user.email))
                        Timber.d("Added uid to Firestore")
                    } else {
                        Timber.d("User id saving error")
                    }
                }
            }
    }
}

interface UserStorage {
    fun registerUserWithEmailAndPassword(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit
    )
}
