package com.example.vshcheglov.webshop.presentation.bought

import android.os.Bundle
import com.example.vshcheglov.webshop.R
import nucleus5.factory.RequiresPresenter
import nucleus5.view.NucleusActivity

@RequiresPresenter(BoughtPresenter::class)
class BoughtActivity : NucleusActivity<BoughtPresenter>(), BoughtPresenter.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bought)
    }
}
