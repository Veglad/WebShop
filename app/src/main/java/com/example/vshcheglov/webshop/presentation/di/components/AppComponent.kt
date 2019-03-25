package com.example.vshcheglov.webshop.presentation.di.components

import com.example.vshcheglov.webshop.data.products.NetworkDataSource
import com.example.vshcheglov.webshop.data.products.ProductRepository
import com.example.vshcheglov.webshop.presentation.basket.BasketPresenter
import com.example.vshcheglov.webshop.presentation.di.modules.*
import com.example.vshcheglov.webshop.presentation.login.LoginPresenter
import com.example.vshcheglov.webshop.presentation.main.MainPresenter
import com.example.vshcheglov.webshop.presentation.registration.RegisterPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        StorageModule::class,
        MappersModule::class,
        FirebaseModule::class
    ]
)
interface AppComponent {
    fun inject(mainPresenter: MainPresenter)
    fun inject(basketPresenter: BasketPresenter)
    fun inject(networkDataSource: NetworkDataSource)
    fun inject(productRepository: ProductRepository)
    fun inject(loginPresenter: LoginPresenter)
    fun inject(registerPresenter: RegisterPresenter)
}