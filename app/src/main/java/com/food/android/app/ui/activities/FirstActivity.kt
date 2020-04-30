package com.food.android.app.ui.activities

import android.os.Bundle
import com.food.android.app.R
import com.food.android.app.ui.activities.base.BaseActivity

class FirstActivity : BaseActivity() {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        initToolbar()
    }
}
