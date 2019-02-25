package com.example.vshcheglov.webshop.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.ui.adapters.ProductsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val products = Klaxon().parseArray<Product>(applicationContext.assets.open("products.json"))
        if (products == null) {
            Toast.makeText(this, "Reading data error", Toast.LENGTH_SHORT).show()
        } else {
            val productsRecyclerAdapter = ProductsRecyclerAdapter(products)
            with(productsRecyclerView) {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = productsRecyclerAdapter
            }
        }
    }
}
