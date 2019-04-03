package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.data.enteties.UserNetwork
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
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

    suspend fun signInUser(email: String, password: String) = suspendCancellableCoroutine<Unit> { continuation ->
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(task.exception!!)
                }
            }
    }

    suspend fun getCurrentUser() = suspendCancellableCoroutine<UserNetwork> { continuation ->
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document -> onGetUserSuccess(document, continuation) }
                .addOnFailureListener {
                    onSaveOrderError(it, continuation, "User fetching error")
                }
        } else {
            onSaveOrderError(Exception("User fetching error"), continuation, "")
        }
    }

    private fun onGetUserSuccess(document: DocumentSnapshot?, continuation: CancellableContinuation<UserNetwork>) {
        val user = document?.toObject(UserNetwork::class.java)
        if (user == null) {
            continuation.resumeWithException(Exception("User parse error"))
        } else {
            continuation.resume(user)
        }
    }

    suspend fun saveOrder(order: Order) = suspendCancellableCoroutine<Unit> { continuation ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            val ordersReference = firestore.collection("users")
                .document(user.uid)
                .collection("orders")
                .document()

            order.id = ordersReference.id

            ordersReference.set(order)
                .addOnSuccessListener { onSaveOrderSuccess(continuation) }
                .addOnFailureListener { onSaveOrderError(it, continuation, "Order saving error") }
        } else {
            onSaveOrderError(Exception("User is not authorized"), continuation, "")
        }
    }

    private fun onSaveOrderSuccess(continuation: CancellableContinuation<Unit>) {
        Timber.d("Order saved successfully")
        continuation.resume(Unit)
    }

    private fun onSaveOrderError(
        throwable: Throwable, continuation: CancellableContinuation<*>,
        exceptionMessage: String
    ) {
        if (continuation.isCancelled || !continuation.isActive) {
            continuation.resumeWithException(CancellationException())
        } else {
            Timber.d("$exceptionMessage: $throwable")
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
                .addOnFailureListener { onSaveOrderError(it, continuation, "Order fetching error") }
        } else {
            onSaveOrderError(Exception("User is not authorized"), continuation, "")
        }
    }

    private fun onGetUserOrdersSuccess(
        continuation: CancellableContinuation<MutableList<Order>>,
        document: QuerySnapshot
    ) {

        val order = document.toObjects(Order::class.java)
        continuation.resume(order)
    }
}