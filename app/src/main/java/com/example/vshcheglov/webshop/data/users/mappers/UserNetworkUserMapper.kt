package com.example.vshcheglov.webshop.data.users.mappers

import com.example.vshcheglov.webshop.data.enteties.UserNetwork
import com.example.vshcheglov.webshop.domain.User
import com.example.vshcheglov.webshop.domain.common.Mapper

class UserNetworkUserMapper: Mapper<UserNetwork?, User?> {

    override fun map(from: UserNetwork?) = from?.let {
        User(it.email, it.id)
    }

    fun map(from: User?) = from?.let {
        UserNetwork(it.email, it.id)
    }
}