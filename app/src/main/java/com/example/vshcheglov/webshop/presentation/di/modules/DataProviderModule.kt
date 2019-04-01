package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.data.DataProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [UserRepositoryModule::class, ProductRepositoryModule::class])
class DataProviderModule {
    @Provides
    @Singleton
    fun provideDataProvider() = DataProvider()
}