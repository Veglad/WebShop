package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.data.enteties.UserNetwork
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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
                        firestore.collection("users").document(user.uid).set(UserNetwork(user.email, user.uid))
                        Timber.d("Added uid to Firestore")
                    } else {
                        Timber.e("UserNetwork id saving error")
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

    fun getCurrentUser(processUser: (user: UserNetwork?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val user = document?.toObject(UserNetwork::class.java)
                    processUser(user)
                }
                .addOnFailureListener {
                    processUser(null)
                }
        } else {
            processUser(null)
        }
    }

    suspend fun saveOrder(order: Order)  = suspendCancellableCoroutine<Unit> { continuation ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            val ordersReference = firestore.collection("users")
                .document(user.uid)
                .collection("orders")
                .document()

            order.id = ordersReference.id

            ordersReference.set(order)
                .addOnSuccessListener {onSaveOrderSuccess(continuation) }
                .addOnFailureListener { onSaveOrderError(it, continuation)}
        } else {
            onSaveOrderError(Exception("User is not authorized"), continuation)
        }
    }

    private fun onSaveOrderSuccess(continuation: CancellableContinuation<Unit>) {
        Timber.d("Order saved successfully")
        continuation.resume(Unit)
    }

    private fun onSaveOrderError(throwable: Throwable, continuation: CancellableContinuation<Unit>) {
        if(continuation.isCancelled || !continuation.isActive) {
            continuation.resumeWithException(CancellationException())
        } else {
            Timber.d("Order fetching error:" + throwable)
            continuation.resumeWithException(throwable)
        }
    }

    fun logOut() {
        firebaseAuth.signOut()
    }

    suspend fun getUserOrders() = suspendCancellableCoroutine<MutableList<Order>> { continuation ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                firestore.collection("users/${currentUser.uid}/orders")
                    .orderBy("timestamp", Query.Direction.DESCENDING) //TODO: change to timestampDate
                    .get()
                    .addOnSuccessListener { document -> onGetUserOrdersSuccess(continuation, document) }
                    .addOnFailureListener { onGetUserOrdersError(continuation, it) }
            } else {
                onGetUserOrdersError(continuation, Exception("User is not authorized"))
            }
        }

    private fun onGetUserOrdersSuccess(
        continuation: CancellableContinuation<MutableList<Order>>,
        document: QuerySnapshot
    ) {

        val order = document.toObjects(Order::class.java)
        continuation.resume(order)
    }

    private fun onGetUserOrdersError(continuation: CancellableContinuation<MutableList<Order>>, throwable: Throwable) {
        if(continuation.isCancelled || !continuation.isActive) {
            continuation.resumeWithException(CancellationException())
        } else {
            Timber.d("Order fetching error:" + throwable)
            continuation.resumeWithException(throwable)
        }
    }
}