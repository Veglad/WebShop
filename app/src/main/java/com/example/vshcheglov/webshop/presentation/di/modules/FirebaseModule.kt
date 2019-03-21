package com.example.vshcheglov.webshop.presentation.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun povideFirebaseAuth() = FirebaseAuth.getInstance()
}