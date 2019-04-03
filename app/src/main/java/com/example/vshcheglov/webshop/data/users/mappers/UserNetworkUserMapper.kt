package com.example.vshcheglov.webshop.data.users.mappers

import com.example.vshcheglov.webshop.data.enteties.UserNetwork
import com.example.vshcheglov.webshop.domain.User
import com.example.vshcheglov.webshop.domain.common.Mapper

class UserNetworkUserMapper: Mapper<UserNetwork, User> {

    override fun map(from: UserNetwork) = User(from.email, from.id)

    fun map(from: User) = UserNetwork(from.email, from.id)
}