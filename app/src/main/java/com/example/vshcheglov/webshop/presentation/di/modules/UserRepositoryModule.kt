package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.data.users.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserRepositoryModule {
    @Provides
    @Singleton
    fun providesRepository(): UserRepository = UserRepository()
}