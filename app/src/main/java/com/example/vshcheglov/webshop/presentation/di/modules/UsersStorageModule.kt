package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
import com.example.vshcheglov.webshop.data.network.WebShopApi
import com.example.vshcheglov.webshop.data.products.NetworkDataSource
import com.example.vshcheglov.webshop.data.products.ProductRepository
import com.example.vshcheglov.webshop.data.users.UserRepository
import com.example.vshcheglov.webshop.data.users.UserStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UsersStorageModule {
    @Provides
    @Singleton
    fun providesRepository(): UserStorage = UserRepository()
}