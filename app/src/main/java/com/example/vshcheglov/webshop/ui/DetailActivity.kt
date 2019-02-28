package com.example.vshcheglov.webshop.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val PRODUCT_KEY = "product_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initWithExtras(intent.extras)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    private fun initWithExtras(bundle: Bundle?) {
        bundle?.let { extras ->
            val product = extras.getParcelable<Product>(PRODUCT_KEY)
            product?.let {
                Glide.with(this@DetailActivity)
                    .load(it.imageThumbnailUrl)
                    .error(R.drawable.no_image)
                    .into(detailProductImageView)
                detailProductTitle.text = it.name
                detailPriceTextView.text = String.format(getString(R.string.price_format), it.price)
                purchasesNumberTextView.text =
                    String.format(getString(R.string.number_of_purchases_format), it.bought.toString())
                detailDescriptionTextView.text = it.longDescription
                if (it.promotional > 0) {
                    detailSaleTextView.visibility = View.VISIBLE
                    detailSaleTextView.text = String.format(getString(R.string.sale_format), it.promotional.toString())
                }
            }
        }
    }
}
