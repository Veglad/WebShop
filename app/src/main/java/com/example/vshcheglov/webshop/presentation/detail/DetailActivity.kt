package com.example.vshcheglov.webshop.presentation.detail

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.basket.BasketActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), DetailPresenter.DetailView {

    companion object {
        const val PRODUCT_KEY = "product_key"
    }

    private val detailPresenter = DetailPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailPresenter.showProductInfo(intent.extras?.getParcelable(DetailActivity.PRODUCT_KEY))

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        detailBuyFloatActionButton.setOnClickListener {
            detailPresenter.buyProduct()
        }
    }

    override fun showProductInfo(product: Product) {
        with(product) {
            Glide.with(this@DetailActivity)
                .load(imageThumbnailUrl)
                .error(R.drawable.no_image)
                .into(detailProductImageView)
            detailProductTitle.text = name
            detailPriceTextView.text = String.format(getString(R.string.price_format), price)
            purchasesNumberTextView.text =
                String.format(getString(R.string.number_of_purchases_format), purchasesNumber.toString())
            detailDescriptionTextView.text = longDescription
            if (percentageDiscount > 0) {
                detailSaleTextView.visibility = View.VISIBLE
                detailSaleTextView.text = String.format(getString(R.string.sale_format), percentageDiscount.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun startBasketActivity() {
        val intent = Intent(this, BasketActivity::class.java)
        startActivity(intent)
    }

    override fun onAttachedToWindow() {
        detailPresenter.onAttached(this)
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        detailPresenter.onDetached()
        super.onDetachedFromWindow()
    }
}
