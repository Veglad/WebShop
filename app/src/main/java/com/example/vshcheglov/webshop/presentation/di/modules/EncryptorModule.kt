package com.example.vshcheglov.webshop.presentation.di.modules

import com.example.vshcheglov.webshop.presentation.helpres.Encryptor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class EncryptorModule(private val keyAlias: String,
                      private val keyStoreName: String,
                      private val cipherTransformation: String) {

    @Provides
    @Singleton
    fun providesEncryptor() = Encryptor(keyAlias, keyStoreName, cipherTransformation)
}