package com.example.vshcheglov.webshop.presentation.di.components

import com.example.vshcheglov.webshop.presentation.basket.BasketPresenter
import com.example.vshcheglov.webshop.presentation.di.modules.AppModule
import com.example.vshcheglov.webshop.presentation.di.modules.BasketActivityModule
import com.example.vshcheglov.webshop.presentation.di.modules.StorageModule
import com.example.vshcheglov.webshop.presentation.di.modules.NetworkModule
import com.example.vshcheglov.webshop.presentation.main.MainActivity
import com.example.vshcheglov.webshop.presentation.main.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        StorageModule::class,
        BasketActivityModule::class
    ]
)
interface AppComponent {
    fun inject(mainPresenter: MainPresenter)
    fun inject(basketPresenter: BasketPresenter)
}