package com.example.vshcheglov.webshop.presentation.basket

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MenuItem
import com.example.vshcheglov.webshop.R
import com.example.vshcheglov.webshop.domain.Basket
import com.example.vshcheglov.webshop.domain.Product
import com.example.vshcheglov.webshop.presentation.basket.adapter.BasketRecyclerAdapter
import com.example.vshcheglov.webshop.presentation.basket.adapter.BasketRecyclerItemTouchHelper
import com.example.vshcheglov.webshop.presentation.entites.BasketPresentation
import com.example.vshcheglov.webshop.presentation.entites.ProductPresentation
import com.example.vshcheglov.webshop.presentation.order.OrderActivity
import kotlinx.android.synthetic.main.activity_basket.*

class BasketActivity : AppCompatActivity(), IBasketView,
    BasketRecyclerItemTouchHelper.BasketRecyclerItemTouchHelperListener {

    lateinit var basketAdapter: BasketRecyclerAdapter
    private var basketPresenter = BasketPresenter(this)

    override val context: Context
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        basketPresenter.onCreate()

        initActionBar()
        basketMakeOrderButton.setOnClickListener { basketPresenter.orderButtonClick() }
    }

    override fun showBasket(basket: BasketPresentation) {
        with(basketRecyclerView) {
            layoutManager = LinearLayoutManager(this@BasketActivity)
            basketAdapter = BasketRecyclerAdapter(this@BasketActivity, basket).also {
                it.onProductNumberIncreasedListener = { position -> basketPresenter.productNumberIncreased(position) }
                it.onProductNumberDecreasedListener = { position -> basketPresenter.productNumberDecreased(position) }
            }
            adapter = basketAdapter
            itemAnimator = DefaultItemAnimator()
            val itemTouchSimpleCallback =
                BasketRecyclerItemTouchHelper(
                    0,
                    ItemTouchHelper.LEFT,
                    this@BasketActivity
                )
            ItemTouchHelper(itemTouchSimpleCallback).attachToRecyclerView(this)
        }
    }

    private fun initActionBar() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val holder = viewHolder as? BasketRecyclerAdapter.ViewHolder
        holder?.let { basketPresenter.onProductItemSwiped(position) }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun startOrderActivity() {
        val intent = Intent(this, OrderActivity::class.java)
        startActivity(intent)
    }

    override fun setBasketAmount(amount: String) {
        basketAmountTextView.text = amount
    }

    override fun setBasketItemsNumber(itemsNumber: String) {
        basketItemsTextView.text = itemsNumber
    }

    override fun showUndo(undoTitle: String) {
        val snackBar = Snackbar.make(basketMainConstraint, undoTitle, Snackbar.LENGTH_SHORT)
        snackBar.setAction(getString(R.string.undo_uppercase)) {basketPresenter.undoPressed()}
        snackBar.show()
    }

    override fun setOrderButtonIsEnabled(isEnabled: Boolean) {
        basketMakeOrderButton.isEnabled = isEnabled
    }

    override fun removeSameProductsCard(position: Int) {
        basketAdapter.removeItem(position)
    }

    override fun restoreSameProductsCard(productToNumberPair: Pair<ProductPresentation, Int>, deletedIndex: Int) {
        basketAdapter.restoreItem(productToNumberPair, deletedIndex)
    }

    override fun setSameProductsNumber(position: Int, number: Int) {
        val view = basketRecyclerView.layoutManager?.findViewByPosition(position)
        view?.let { basketAdapter.setProductsNumberByPosition(it, number, position) }
    }

    override fun setTotalProductPrice(position: Int, totalDiscountPrice: Double) {
        val view = basketRecyclerView.layoutManager?.findViewByPosition(position)
        view?.let { basketAdapter.initTotalProductsPrice(it, totalDiscountPrice) }
    }

    override fun setTotalProductPriceTitle(position: Int, totalPrice: Double) {
        val view = basketRecyclerView.layoutManager?.findViewByPosition(position)
        view?.let { basketAdapter.initTotalProductsPriceTitle(it, totalPrice) }
    }
}
