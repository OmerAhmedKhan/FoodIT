package com.food.android.app.ui.activities.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.food.android.app.R
import com.food.android.app.ui.activities.FirstActivity
import com.food.android.app.ui.activities.LoginActivity
import com.food.android.app.ui.activities.NotificationsActivity
import com.food.android.app.utils.MyHelper
import com.food.android.app.utils.SharedPreferences
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.io.File

open class BaseActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null

    protected lateinit var drawerLayout: DrawerLayout
    private var drawerToggle: ActionBarDrawerToggle? = null
    var navigationView: NavigationView? = null
    var mHeaderView: View?=null
    protected lateinit var mContext: Context

    var textViewUsername: TextView?=null
    var textViewEmail: TextView?=null

    val RC_STORAGE_PERMS1 = 101
    val RC_SELECT_PICTURE = 103
    val ACTION_BAR_TITLE = "action_bar_title"
    var imageFile: File ?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this@BaseActivity
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    protected fun initToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val titleTv = toolbar!!.findViewById<View>(R.id.toolbar_title) as TextView
        titleTv.text = this.title.toString()
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar!!.navigationIcon!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }



    protected fun initToolbarWithoutBack() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val titleTv = toolbar!!.findViewById<View>(R.id.toolbar_title) as TextView
        titleTv.text = this.title.toString()
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    protected fun setUpNav() {
        drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }
        }

        drawerLayout.addDrawerListener(drawerToggle!!)

        navigationView = findViewById<View>(R.id.nav_view) as NavigationView


          mHeaderView =  navigationView!!.getHeaderView(0);

        textViewUsername = mHeaderView?.findViewById(R.id.userName);
        textViewEmail= mHeaderView?.findViewById(R.id.textViewEmail);


        if(!SharedPreferences.getUserName().isNullOrBlank())
            textViewUsername?.setText(SharedPreferences.getUserName())
        else
            textViewUsername?.setText(FirebaseAuth.getInstance().currentUser?.displayName)

        textViewEmail?.setText(FirebaseAuth.getInstance().currentUser?.email)

        // Setting Navigation View Item Selected Listener to handle the item
        // click of the navigation menu
        navigationView!!.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            // Checking if the item is in checked state or not, if not make
            // it in checked state
            if (menuItem.isChecked)
                menuItem.isChecked = false
            else
                menuItem.isChecked = true

            // Closing drawer on item click
            drawerLayout.closeDrawers()

            // Check to see which item was being clicked and perform
            // appropriate action
            val intent: Intent
            when (menuItem.itemId) {

               /* R.id.nav_activity1 -> {
                    startActivity(Intent(applicationContext, FirstActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }*/

                R.id.nav_activity2 -> {
                    startActivity(Intent(applicationContext, NotificationsActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }

                R.id.logout -> {
                    SharedPreferences.setLoginStatus(false)
                    com.food.android.app.utils.SharedPreferences.setFirstTimeList(false)
                    LoginActivity.mGoogleSignInClient?.signOut()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                }
            }
            false
        })

        // Setting the actionbarToggle to drawer layout

        // calling sync state is necessay or else your hamburger icon wont show
        // up
        drawerToggle!!.syncState()

    }

    public override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle!!.onConfigurationChanged(newConfig)
    }

    //to enable option menu open this code of block

    /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == R.id.menu1) {
            true
        } else super.onOptionsItemSelected(item)

    }

    protected fun startAnimatedActivity(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_STORAGE_PERMS1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPicture()
            } else {
                MyHelper.needPermission(this, requestCode, R.string.permission_request)
            }
        }
    }

    fun checkStoragePermission(requestCode: Int) {
        when (requestCode) {
            RC_STORAGE_PERMS1 -> {
                val hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                    selectPicture()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), requestCode)
                }
            }
        }
    }

    private fun selectPicture() {
        imageFile = MyHelper.createTempFile(imageFile)
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_SELECT_PICTURE)
    }


}