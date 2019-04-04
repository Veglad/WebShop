package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.data.enteties.mappers.*
import com.example.vshcheglov.webshop.data.enteties.mappers.ResponseUserMapper
import com.example.vshcheglov.webshop.presentation.entites.mappers.ProductBasketCardMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MappersModule {

    @Singleton
    @Provides
    fun povideProductBasketCardMapper() = ProductBasketCardMapper()

    @Singleton
    @Provides
    fun provideNetworkProductMapper() = ResponseProductMapper()

    @Singleton
    @Provides
    fun provideBasketToOrderMapper() = BasketToOrderMapper()

    @Singleton
    @Provides
    fun provideUserNetworkUserMapper() = ResponseUserMapper()

    @Singleton
    @Provides
    fun provideRealmProductProductMapper() = RealmProductMapper()


    @Singleton
    @Provides
    fun provideOrderNetworkOrderMapper() = ResponseOrderMapper()

    @Singleton
    @Provides
    fun provideRealmOrderOrderNetworkMapper() = RealmResponseOrderMapper()

    @Singleton
    @Provides
    fun provideRealmOrderOrderMapper() = RealmOrderMapper()

    @Singleton
    @Provides
    fun provideRealmOrderProductMapper() = RealmOrderProductMapper()

    @Singleton
    @Provides
    fun provideResponseOrderProductMapper() = ResponseOrderProductMapper()

    @Singleton
    @Provides
    fun provideRealmResponseOrderProductMapper() = RealmResponseOrderProductMapper()
}
