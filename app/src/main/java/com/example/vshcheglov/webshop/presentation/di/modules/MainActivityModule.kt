package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.data.products.ProductRepository
import com.example.vshcheglov.webshop.presentation.main.MainPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class MainActivityModule {
    @Provides
    @Singleton
    fun providesPresenter(repository: ProductRepository) = MainPresenter(repository)
}