package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.data.enteties.RealmOrder
import io.realm.Realm
import io.realm.RealmList

class UserStorage {
    fun saveOrders(realmOrderList: MutableList<RealmOrder>) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                val managedOrderList = RealmList<RealmOrder>()
                managedOrderList.addAll(realmOrderList)
                transactionRealm.insertOrUpdate(realmOrderList)
            }
        }
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