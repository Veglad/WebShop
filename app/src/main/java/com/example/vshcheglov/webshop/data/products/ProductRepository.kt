package com.example.vshcheglov.webshop.data.products

import com.example.vshcheglov.webshop.App
import com.example.vshcheglov.webshop.data.enteties.RealmProduct
import com.example.vshcheglov.webshop.data.enteties.mappers.RealmAllProductsAllProductsMapper
import com.example.vshcheglov.webshop.data.enteties.mappers.RealmProductProductMapper
import com.example.vshcheglov.webshop.domain.AllProducts
import com.example.vshcheglov.webshop.domain.Product
import io.realm.Realm
import io.realm.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepository {

    @Inject
    lateinit var networkDataSource: ProductNetworkDataSource
    @Inject
    lateinit var productMapper: RealmProductProductMapper
    @Inject
    lateinit var allProductsMapper: RealmAllProductsAllProductsMapper

    init {
        App.appComponent.inject(this)
    }

    suspend fun getProducts() = withContext(Dispatchers.IO) {
        var productList: MutableList<Product>
        try {
            productList = networkDataSource.getProducts()
            saveProductsToDb(productList)
            productList
        } catch (e: Exception) {
            productList = getProductsFromDb()
            productList
        }
    }

    private fun saveProductsToDb(productList: MutableList<Product>) {
        val realmProductList = mutableListOf<RealmProduct>().apply {
            for (product in productList) {
                add(productMapper.map(product))
            }
        }

        Realm.getDefaultInstance().use { realm ->
            realm.executeTransactionAsync { transactionRealm ->
                val managerProductList = RealmList<RealmProduct>()
                managerProductList.addAll(realmProductList)
                transactionRealm.insertOrUpdate(realmProductList)
            }
        }
    }

    private fun getProductsFromDb(isPromotional: Boolean = false): MutableList<Product> {

        var productList: MutableList<Product> = mutableListOf()
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                val managedProducts = if(!isPromotional) {
                    transactionRealm.where(RealmProduct::class.java).findAll()
                } else {
                    transactionRealm.where(RealmProduct::class.java)
                        .greaterThan("percentageDiscount", 0).findAll()
                }

                productList = mutableListOf<Product>().apply {
                    for (realmProduct in managedProducts) {
                        add(productMapper.map(realmProduct))
                    }
                }
            }
        }

        return productList
    }

    suspend fun getPromotionalProducts() = withContext(Dispatchers.IO) {
        var productList: MutableList<Product>
        try {
            productList = networkDataSource.getPromotionalProducts()
            saveProductsToDb(productList)
            productList
        } catch (e: Exception) {
            productList = getProductsFromDb(true)
            productList
        }
    }

    suspend fun getAllProducts() = networkDataSource.getAllProducts()
}