package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.presentation.entites.mappers.ProductBasketCardMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BasketActivityModule {

    @Singleton
    @Provides
    fun povideProductBasketCardMapper() = ProductBasketCardMapper()
}
