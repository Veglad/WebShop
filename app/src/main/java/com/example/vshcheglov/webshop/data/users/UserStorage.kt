package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.data.enteties.RealmOrder
import io.realm.Realm

class UserStorage {
    fun saveOrders(realmOrderlist: MutableList<RealmOrder>) {

    }

    fun clear() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.deleteAll()
            }
        }
    }

    fun getOrders() : MutableList<RealmOrder>{
        return  mutableListOf()
    }
}