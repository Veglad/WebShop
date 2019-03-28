package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

class UserRepository : UserStorage {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var firestore: FirebaseFirestore

    override val isSignedIn: Boolean
        get() = firebaseAuth.currentUser != null

    init {
        App.appComponent.inject(this)
    }

    override fun registerUserWithEmailAndPassword(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                completeCallback(task)
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        firestore.collection("users").document(user.uid).set(User(user.email, user.uid))
                        Timber.d("Added uid to Firestore")
                    } else {
                        Timber.d("User id saving error")
                    }
                }
            }
    }

    override fun signInUserWithEmailAndPassword(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> completeCallback(task) }
    }

    override fun getCurrentUser(processUser: (user: User?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val user = document?.toObject(User::class.java)
                    processUser(user)
                }
                .addOnFailureListener {
                    processUser(null)
                }
        } else {
            processUser(null)
        }
    }

    override fun logOut() {
        firebaseAuth.signOut()
    }
}

interface UserStorage {
    val isSignedIn: Boolean

    fun getCurrentUser(processUser: (user: User?) -> Unit)

    fun registerUserWithEmailAndPassword(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit
    )

    fun signInUserWithEmailAndPassword(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit
    )

    fun logOut()
}
