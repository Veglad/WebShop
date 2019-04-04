package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.RealmOrder
import com.example.vshcheglov.webshop.data.enteties.mappers.ResponseOrderMapper
import com.example.vshcheglov.webshop.data.enteties.mappers.RealmResposeOrderMapper
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.data.users.mappers.UserNetworkUserMapper
import javax.inject.Inject


class UserRepository {

    @Inject
    lateinit var userNetwork: UserNetworkDataSource
    @Inject
    lateinit var mapper: UserNetworkUserMapper
    @Inject
    lateinit var userStorage: UserStorage
    @Inject
    lateinit var orderNetworkOrderMapper: ResponseOrderMapper
    @Inject
    lateinit var realmOrderNetworkOrderMapper: RealmResposeOrderMapper

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
        userStorage.clear()
    }

    suspend fun getUserOrders(): MutableList<Order> {
        var orderList = mutableListOf<Order>()
        try {
            val networkOrders = userNetwork.getUserOrders()

            orderList.apply {
                for (orderNetwork in networkOrders) {
                    add(orderNetworkOrderMapper.map(orderNetwork))
                }
            }

            val realmOrderList = mutableListOf<RealmOrder>().apply {
                for (orderNetwork in networkOrders) {
                    add(realmOrderNetworkOrderMapper.map(orderNetwork))
                }
            }

            userStorage.saveOrders(realmOrderList)
        } catch (ex: Exception) {
            orderList = userStorage.getUserOrders()
        }

        return orderList
    }
}
