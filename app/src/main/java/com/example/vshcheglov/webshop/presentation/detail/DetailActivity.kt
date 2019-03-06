package com.example.vshcheglov.webshop.presentation.detail

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.BasketActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val PRODUCT_KEY = "product_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val product = getProduct(intent.extras)
        product?.let {
            initUiViaProduct(it)
            initBuyButton(it)
        }

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    private fun getProduct(bundle: Bundle?) = bundle?.getParcelable<Product>(PRODUCT_KEY)

    private fun initUiViaProduct(product: Product) {
        with(product) {
            Glide.with(this@DetailActivity)
                .load(imageThumbnailUrl)
                .error(R.drawable.no_image)
                .into(detailProductImageView)
            detailProductTitle.text = name
            detailPriceTextView.text = String.format(getString(R.string.price_format), price)
            purchasesNumberTextView.text =
                String.format(getString(R.string.number_of_purchases_format), bought.toString())
            detailDescriptionTextView.text = longDescription
            if (promotional > 0) {
                detailSaleTextView.visibility = View.VISIBLE
                detailSaleTextView.text = String.format(getString(R.string.sale_format), promotional.toString())
            }
        }
    }

    private fun initBuyButton(product: Product) {
        detailBuyFloatActionButton.setOnClickListener {
            Basket.addProduct(product)
            val intent = Intent(this, BasketActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}
