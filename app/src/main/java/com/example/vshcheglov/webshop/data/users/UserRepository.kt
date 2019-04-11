package com.example.vshcheglov.webshop.data.users

import android.graphics.Bitmap
import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.RealmOrder
import com.example.vshcheglov.webshop.data.enteties.mappers.ResponseOrderMapper
import com.example.vshcheglov.webshop.data.enteties.mappers.RealmResponseOrderMapper
import com.example.vshcheglov.webshop.data.enteties.mappers.RealmUserCredentialsMapper
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.data.enteties.mappers.ResponseUserMapper
import com.example.vshcheglov.webshop.domain.UserCredentials
import javax.inject.Inject


class UserRepository {

    @Inject
    lateinit var userNetwork: UserNetworkDataSource
    @Inject
    lateinit var mapper: ResponseUserMapper
    @Inject
    lateinit var userStorage: UserStorage
    @Inject
    lateinit var responseOrderMapper: ResponseOrderMapper
    @Inject
    lateinit var realmResponseOrderMapper: RealmResponseOrderMapper
    @Inject
    lateinit var realmUserCredentialsMapper: RealmUserCredentialsMapper

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
        val orderNetwork = responseOrderMapper.map(order)
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
                    add(responseOrderMapper.map(orderNetwork))
                }
            }

            val realmOrderList = mutableListOf<RealmOrder>().apply {
                for (orderNetwork in networkOrders) {
                    add(realmResponseOrderMapper.map(orderNetwork))
                }
            }

            userStorage.saveOrders(realmOrderList)
        } catch (ex: Exception) {
            orderList = userStorage.getUserOrders()
        }

        return orderList
    }

    fun saveUserProfilePhoto(profilePhotoBitmap: Bitmap, name: String) {
        userNetwork.saveUserProfilePhoto(profilePhotoBitmap, name)
    }

    suspend fun getUserAvatarByteArray() = userNetwork.getUserAvatarByteArray()

    fun saveUserCredentialsLocal(userCredentials: UserCredentials) {
        userStorage.saveUserCredentialsLocal(realmUserCredentialsMapper.map(userCredentials))
    }

    fun getUserCredentials() : UserCredentials? {
        val realmCredentials = userStorage.getUserCredentials()
        return realmCredentials?.let {  realmUserCredentialsMapper.map(it) }
    }

    fun containsUserCredentials() = userStorage.containsUserCredentials()

    fun deleteUserCredentials() {
        userStorage.deleteUserCredentials()
    }
}
