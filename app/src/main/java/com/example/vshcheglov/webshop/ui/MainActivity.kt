package com.example.vshcheglov.webshop.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.data.getFakeProducts
import com.example.vshcheglov.webshop.ui.adapters.ProductsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val products = getFakeProducts(applicationContext)
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
