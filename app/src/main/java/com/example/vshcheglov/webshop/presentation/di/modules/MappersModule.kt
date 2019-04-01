package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.data.enteties.mappers.BasketToOrderMapper
import com.example.vshcheglov.webshop.data.enteties.mappers.ProductEntityDataMapper
import com.example.vshcheglov.webshop.data.enteties.mappers.RealmProductProductMapper
import com.example.vshcheglov.webshop.data.users.mappers.UserNetworkUserMapper
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
}
