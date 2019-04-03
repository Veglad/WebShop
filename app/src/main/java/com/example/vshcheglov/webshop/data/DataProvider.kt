package com.example.vshcheglov.webshop.data

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.data.products.ProductRepository
import com.example.vshcheglov.webshop.data.users.UserRepository
import com.example.vshcheglov.webshop.domain.User
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

    fun getCurrentUser(processUser: (user: User?) -> Unit) {
        userRepository.getCurrentUser(processUser)
    }

    suspend fun saveOrder(order: Order) {
        userRepository.saveOrder(order)
    }

    suspend fun logOut() {
        userRepository.logOut()
    }

    suspend fun getUserOrders()  = userRepository.getUserOrders()

}