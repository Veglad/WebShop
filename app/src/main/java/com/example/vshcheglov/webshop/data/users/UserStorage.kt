package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.data.enteties.RealmOrder

class UserStorage {
    fun saveOrders(realmOrderlist: MutableList<RealmOrder>) {

    }

    fun clear() {

    }

    fun getOrders() : MutableList<RealmOrder>{
        return  mutableListOf()
    }
}