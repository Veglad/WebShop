package com.example.vshcheglov.webshop.data.users

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.RealmOrder
import com.example.vshcheglov.webshop.data.enteties.RealmUserCredentials
import com.example.vshcheglov.webshop.data.enteties.mappers.RealmOrderMapper
import com.example.vshcheglov.webshop.domain.Order
import com.example.vshcheglov.webshop.domain.UserCredentials
import io.realm.Realm
import io.realm.RealmList
import javax.inject.Inject

class UserStorage {

    @Inject
    lateinit var mapper: RealmOrderMapper

    init {
        App.appComponent.inject(this)
    }

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

    fun getUserOrders(): MutableList<Order> {
        var realmOrderList: MutableList<Order> = mutableListOf()
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                val realmResults = transactionRealm.where(RealmOrder::class.java)
                    .findAll()
                realmOrderList = mutableListOf<Order>().apply {
                    for (realmResult in realmResults) {
                        add(mapper.map(realmResult))
                    }
                }
            }
        }

        return realmOrderList
    }

    fun saveUserCredentialsLocal(realmUserCredentials: RealmUserCredentials) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                realm.insertOrUpdate(realmUserCredentials)
            }
        }
    }

    fun containsUserCredentials(): Boolean {
        var containsCredentials = false
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                containsCredentials = transactionRealm.where(RealmUserCredentials::class.java)
                    .findAll().isNotEmpty()
            }
        }

        return containsCredentials
    }

    fun getUserCredentials(): RealmUserCredentials? {
        var realmUserCredentials: RealmUserCredentials? = null
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                val realmResults = transactionRealm.where(RealmUserCredentials::class.java)
                    .findFirst()
                realmResults?.let {
                    realmUserCredentials = RealmUserCredentials(realmResults.email, realmResults.encryptedPassword)
                }
            }
        }

        return realmUserCredentials
    }

    fun deleteUserCredentials() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                val realmResults = transactionRealm.where(RealmUserCredentials::class.java)
                    .findFirst()
                realmResults?.deleteFromRealm()
            }
        }
    }
}