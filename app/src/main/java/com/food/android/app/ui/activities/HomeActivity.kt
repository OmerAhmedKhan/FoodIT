package com.food.android.app.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import com.firebase.client.Firebase
import com.food.android.app.R
import com.food.android.app.adapters.ViewPagerAdapter
import com.food.android.app.ui.activities.base.BaseActivity
import com.food.android.app.ui.fragments.CountryFragment
import com.food.android.app.ui.fragments.CommunityFragment
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_home.*




class HomeActivity : BaseActivity() {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val inflater = this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflater.inflate(R.layout.activity_home, null, false)
        drawerLayout.addView(contentView, 0)

        Firebase.setAndroidContext(this)

        initToolbar()
        setUpNav()
        setupTabs()

        floatingActionButton.setOnClickListener {
            startActivity(Intent(applicationContext, AddPostActivity::class.java))
        }


        //FirebaseMessaging.getInstance().subscribeToTopic("/topics/test")
    }

    private fun setupTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(CommunityFragment(), "Community")
        adapter.addFragment(CountryFragment(), "Country")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        viewPager.setOffscreenPageLimit(tabs.tabCount);
    }
}