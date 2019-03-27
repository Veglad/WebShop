package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class UserRepository {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase

    init {
        App.appComponent.inject(this)
    }
}