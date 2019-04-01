package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.Order
import com.example.vshcheglov.webshop.data.enteties.User
import com.example.vshcheglov.webshop.data.users.network.UserNetworkDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class UserRepository {

    @Inject
    lateinit var userNetwork: UserNetworkDataSource

    val isSignedIn: Boolean
        get() = userNetwork.isSignedIn

    init {
        App.appComponent.inject(this)
    }

    fun registerUser(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit
    ) {
        userNetwork.registerUser(email, password, completeCallback)
    }

    fun signInUser(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit
    ) {
        userNetwork.signInUser(email, password, completeCallback)
    }

    fun getCurrentUser(processUser: (user: User?) -> Unit) {
        userNetwork.getCurrentUser(processUser)
    }

    fun saveOrder(order: Order, onResult: (exception: Exception?) -> Unit) {
        userNetwork.saveOrder(order, onResult)
    }

    fun logOut() {
        userNetwork.logOut()
    }

    fun getUserOrders(processOrders: (orderList: MutableList<Order>?) -> Unit) {
        userNetwork.getUserOrders(processOrders)
    }
}
