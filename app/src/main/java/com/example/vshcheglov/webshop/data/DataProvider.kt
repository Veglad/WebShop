package com.example.vshcheglov.webshop.data

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.OrderNetwork
import com.example.vshcheglov.webshop.data.enteties.UserNetwork
import com.example.vshcheglov.webshop.data.products.ProductRepository
import com.example.vshcheglov.webshop.data.users.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class DataProvider {
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var productRepository: ProductRepository

    val isSignedIn: Boolean
        get() = userRepository.isSignedIn

    init {
        App.appComponent.inject(this)
    }

    suspend fun getProducts() = productRepository.getProducts()

    suspend fun getPromotionalProducts() = productRepository.getPromotionalProducts()

    suspend fun getAllProducts() = productRepository.getAllProducts()

    fun registerUser(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit
    ) {
        userRepository.registerUser(email, password, completeCallback)
    }

    fun signInUser(
        email: String, password: String,
        completeCallback: (task: Task<AuthResult>) -> Unit
    ) {
        userRepository.signInUser(email, password, completeCallback)
    }

    fun getCurrentUser(processUser: (user: UserNetwork?) -> Unit) {
        userRepository.getCurrentUser(processUser)
    }

    fun saveOrder(order: OrderNetwork, onResult: (exception: Exception?) -> Unit) {
        userRepository.saveOrder(order, onResult)
    }

    fun logOut() {
        userRepository.logOut()
    }

    fun getUserOrders(processOrders: (orderList: MutableList<OrderNetwork>?) -> Unit) {
        userRepository.getUserOrders(processOrders)
    }
}