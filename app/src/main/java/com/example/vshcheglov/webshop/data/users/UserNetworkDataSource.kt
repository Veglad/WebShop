package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.Order
import com.example.vshcheglov.webshop.data.enteties.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import timber.log.Timber
import javax.inject.Inject

class UserNetworkDataSource {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var firestore: FirebaseFirestore

    val isSignedIn: Boolean
        get() = firebaseAuth.currentUser != null

    init {
        App.appComponent.inject(this)
    }

    fun registerUser(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit
    ) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                completeCallback(task)
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        firestore.collection("users").document(user.uid).set(User(user.email, user.uid))
                        Timber.d("Added uid to Firestore")
                    } else {
                        Timber.e("User id saving error")
                    }
                }
            }
    }

    fun signInUser(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> completeCallback(task) }
    }

    fun getCurrentUser(processUser: (user: User?) -> Unit) {
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

    fun saveOrder(order: Order, onResult: (exception: Exception?) -> Unit) {

        val user = firebaseAuth.currentUser
        if (user != null) {
            val ordersReference = firestore.collection("users")
                .document(user.uid)
                .collection("orders")
                .document()

            order.id = ordersReference.id

            ordersReference.set(order)
                .addOnSuccessListener {
                    Timber.d("Order saved successfully")
                    onResult(null)
                }
                .addOnFailureListener {
                    Timber.e("User order saving error: " + it)
                    onResult(it)
                }
        } else {
            Timber.e("User order saving error")
            onResult(java.lang.Exception("User not authenticated."))
        }
    }

    fun logOut() {
        firebaseAuth.signOut()
    }

    fun getUserOrders(processOrders: (orderList: MutableList<Order>?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("users/${currentUser.uid}/orders")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { document ->
                    Timber.d("Order fetched successfully")
                    val order = document?.toObjects(Order::class.java)
                    processOrders(order)
                }
                .addOnFailureListener {
                    Timber.d("Order fetching error:" + it)
                    processOrders(null)
                }
        } else {
            processOrders(null)
        }
    }
}