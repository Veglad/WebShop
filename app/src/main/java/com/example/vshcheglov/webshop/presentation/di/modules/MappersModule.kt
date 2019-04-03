package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.data.enteties.mappers.*
import com.example.vshcheglov.webshop.data.users.mappers.UserNetworkUserMapper
import com.example.vshcheglov.webshop.presentation.entites.mappers.ProductBasketCardMapper
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class MappersModule {

    @Singleton
    @Provides
    fun povideProductBasketCardMapper() = ProductBasketCardMapper()

    @Singleton
    @Provides
    fun provideNetworkProductMapper() = ProductEntityDataMapper()

    @Singleton
    @Provides
    fun provideBasketToOrderMapper() = BasketToOrderMapper()

    @Singleton
    @Provides
    fun provideUserNetworkUserMapper() = UserNetworkUserMapper()

    @Singleton
    @Provides
    fun provideRealmProductProductMapper() = RealmProductProductMapper()


    @Singleton
    @Provides
    fun provideOrderNetworkOrderMapper() = OrderNetworkOrderMapper()

    @Singleton
    @Provides
    fun provideRealmOrderOrderNetworkMapper() = RealmOrderOrderNetworkMapper()

    @Singleton
    @Provides
    fun provideRealmOrderOrderMapper() = RealmOrderOrderMapper()
}
