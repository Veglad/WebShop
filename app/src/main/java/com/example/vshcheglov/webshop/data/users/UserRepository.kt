package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.data.users.mappers.UserNetworkUserMapper
import com.example.vshcheglov.webshop.domain.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserRepository {

    @Inject
    lateinit var userNetwork: UserNetworkDataSource
    @Inject
    lateinit var mapper: UserNetworkUserMapper
    @Inject
    lateinit var userStorage: UserStorage

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

    suspend fun signInUser(email: String, password: String) {
        userNetwork.signInUser(email, password)
    }

    suspend fun getCurrentUser() = mapper.map(userNetwork.getCurrentUser())

    suspend fun saveOrder(order: Order) {
        userNetwork.saveOrder(order)
        //TODO: Map
    }

    fun logOut() {
        userNetwork.logOut()
        //TODO: Clear realm database
    }

    suspend fun getUserOrders() : MutableList<Order> {
        return userNetwork.getUserOrders()
        //TODO: Map
    }
}
