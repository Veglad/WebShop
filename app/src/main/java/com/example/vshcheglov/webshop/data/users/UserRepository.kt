package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.OrderNetwork
import com.example.vshcheglov.webshop.data.enteties.UserNetwork
import com.example.vshcheglov.webshop.data.users.mappers.UserNetworkUserMapper
import com.example.vshcheglov.webshop.domain.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
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

    fun signInUser(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit
    ) {
        userNetwork.signInUser(email, password, completeCallback)
    }

    fun getCurrentUser(processUser: (user: User?) -> Unit) {
        userNetwork.getCurrentUser { userNetwork ->
            processUser(mapper.map(userNetwork))
        }
    }

    fun saveOrder(order: OrderNetwork, onResult: (exception: Exception?) -> Unit) {
        userNetwork.saveOrder(order, onResult)
    }

    fun logOut() {
        userNetwork.logOut()
        //TODO: Clear realm database
    }

    fun getUserOrders(processOrders: (orderList: MutableList<OrderNetwork>?) -> Unit) {
        userNetwork.getUserOrders(processOrders)
    }
}
