package com.example.vshcheglov.webshop.data.enteties.mappers

import com.example.vshcheglov.webshop.data.enteties.RealmUserCredentials
import com.example.vshcheglov.webshop.domain.UserCredentials
import com.example.vshcheglov.webshop.domain.common.Mapper

class RealmUserCredentialsMapper : Mapper<RealmUserCredentials, UserCredentials> {

    override fun map(from: RealmUserCredentials) = UserCredentials(from.email, from.encryptedPassword)

    fun map(from: UserCredentials) = RealmUserCredentials(from.email, from.encryptedPassword)
}