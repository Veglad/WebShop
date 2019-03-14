package com.example.vshcheglov.webshop.presentation.di.components

import com.example.vshcheglov.webshop.presentation.di.modules.AppModule
import com.example.vshcheglov.webshop.presentation.di.modules.MainActivityModule
import com.example.vshcheglov.webshop.presentation.di.modules.NetworkModule
import com.example.vshcheglov.webshop.presentation.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, MainActivityModule::class])
interface ProductsComponent {
    fun inject(mainActivity: MainActivity)
}