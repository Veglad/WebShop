package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.data.products.ProductRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class ProductStorageModule {
    @Provides
    @Singleton
    fun providesRepository() = ProductRepository()
}