package com.example.vshcheglov.webshop.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.ui.adapters.ProductsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val productsRecyclerAdapter = ProductsRecyclerAdapter(getProducts())
        with(productsRecyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productsRecyclerAdapter
        }
    }

    private fun getProducts() = mutableListOf<Product>().apply {
        add(
            Product(
                0,
                "Iphone X",
                "Some description about IPhone",
                1099.0,
                "https://i.allo.ua/media/catalog/product/cache/1/image/425x295/799896e5c6c37e11608b9f8e1d047d15/a/p/apple_iphone_x_64gb_mqac2_space_grey_4.jpg"
            )
        )
        add(
            Product(
                1,
                "Iphone XS",
                "Some description about IPhone XS",
                1200.0,
                "https://hotline.ua/img/tx/178/178801049_s265.jpg"
            )
        )
        add(
            Product(
                0,
                "Iphone X",
                "Some description about IPhone",
                1099.0,
                "https://i.allo.ua/media/catalog/product/cache/1/image/425x295/799896e5c6c37e11608b9f8e1d047d15/a/p/apple_iphone_x_64gb_mqac2_space_grey_4.jpg"
            )
        )
        add(
            Product(
                1,
                "Iphone XS",
                "Some description about IPhone XS",
                1200.0,
                "https://hotline.ua/img/tx/178/178801049_s265.jpg"
            )
        )
        add(
            Product(
                0,
                "Iphone X",
                "Some description about IPhone",
                1099.0,
                "https://i.allo.ua/media/catalog/product/cache/1/image/425x295/799896e5c6c37e11608b9f8e1d047d15/a/p/apple_iphone_x_64gb_mqac2_space_grey_4.jpg"
            )
        )
        add(
            Product(
                1,
                "Iphone XS",
                "Some description about IPhone XS",
                1200.0,
                "https://hotline.ua/img/tx/178/178801049_s265.jpg"
            )
        )
    }
}
