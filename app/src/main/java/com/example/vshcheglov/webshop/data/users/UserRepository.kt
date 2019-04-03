package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.mappers.OrderNetworkOrderMapper
import com.example.vshcheglov.webshop.data.enteties.mappers.RealmOrderOrderMapper
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
    @Inject
    lateinit var orderNetworkOrderMapper: OrderNetworkOrderMapper
    @Inject
    lateinit var realmOrderNetworkOrderMapper: OrderNetworkOrderMapper
    @Inject
    lateinit var realmOrderOrderMapper: RealmOrderOrderMapper

    val isSignedIn: Boolean
        get() = userNetwork.isSignedIn

    init {
        App.appComponent.inject(this)
    }

    suspend fun registerUser(email: String, password: String) {
        userNetwork.registerUser(email, password)
    }

    suspend fun signInUser(email: String, password: String) {
        userNetwork.signInUser(email, password)
    }

    suspend fun getCurrentUser() = mapper.map(userNetwork.getCurrentUser())

    suspend fun saveOrder(order: Order) {
        val orderNetwork = orderNetworkOrderMapper.map(order)
        userNetwork.saveOrder(orderNetwork)
    }

    fun logOut() {
        userNetwork.logOut()
        //TODO: Clear realm database
    }

    suspend fun getUserOrders() : MutableList<Order> {
        val networkOrders = userNetwork.getUserOrders()

        val orderList = mutableListOf<Order>().apply {
            for (orderNetwork in networkOrders) {
                add(orderNetworkOrderMapper.map(orderNetwork))
            }
        }
        return orderList
    }
}
