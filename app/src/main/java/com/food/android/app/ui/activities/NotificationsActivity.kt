package com.food.android.app.ui.activities

import android.os.Bundle
import com.food.android.app.R
import com.food.android.app.ui.activities.base.BaseActivity
import android.widget.Toast
import android.view.View
import kotlinx.android.synthetic.main.activity_notifications.*
import android.widget.CheckBox
import com.food.android.app.utils.SharedPreferences
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Created by Aleesha Kanwal on 3/21/2020.
 */
class NotificationsActivity : BaseActivity(){

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        initToolbar()

        bbq.isChecked = SharedPreferences.isBBQ()
        desi.isChecked = SharedPreferences.isDesi()
        mocktails.isChecked = SharedPreferences.isMocktails()
        chineese.isChecked = SharedPreferences.isChineese()
        desserts.isChecked = SharedPreferences.isDesserts()
        steaks.isChecked = SharedPreferences.isSteaks()
        seafood.isChecked = SharedPreferences.isSeaFood()
        icecream.isChecked = SharedPreferences.isIcecream()
        diet.isChecked = SharedPreferences.isDietFood()

        bbq.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                subscribeToTopic(bbq.text.toString())
                SharedPreferences.setBBQStatus(isChecked)
            } else {
                SharedPreferences.setBBQStatus(isChecked)
                unSubscribeToTopic(bbq.text.toString())
            }
        })


        desi.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                subscribeToTopic(desi.text.toString())
                SharedPreferences.setDesiStatus(isChecked)
            } else {
                SharedPreferences.setDesiStatus(isChecked)
                unSubscribeToTopic(desi.text.toString())
            }
        })



        mocktails.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                subscribeToTopic(mocktails.text.toString())
                SharedPreferences.setMocktailsStatus(isChecked)
            } else {
                SharedPreferences.setMocktailsStatus(isChecked)
                unSubscribeToTopic(mocktails.text.toString())
            }
        })


        chineese.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                subscribeToTopic(chineese.text.toString())
                SharedPreferences.setChineeseStatus(isChecked)
            } else {
                SharedPreferences.setChineeseStatus(isChecked)
                unSubscribeToTopic(chineese.text.toString())
            }
        })

        desserts.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                subscribeToTopic(desserts.text.toString())
                SharedPreferences.setDessertsStatus(isChecked)
            } else {
                SharedPreferences.setDessertsStatus(isChecked)
                unSubscribeToTopic(desserts.text.toString())
            }
        })



        steaks.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                subscribeToTopic(steaks.text.toString())
                SharedPreferences.setSteaksStatus(isChecked)
            } else {
                SharedPreferences.setSteaksStatus(isChecked)
                unSubscribeToTopic(steaks.text.toString())
            }
        })


        seafood.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                subscribeToTopic(seafood.text.toString())
                SharedPreferences.setSeaFoodStatus(isChecked)
            } else {
                SharedPreferences.setSeaFoodStatus(isChecked)
                unSubscribeToTopic(seafood.text.toString())
            }
        })



        icecream.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                subscribeToTopic(icecream.text.toString())
                SharedPreferences.setIcecreamStatus(isChecked)
            } else {
                SharedPreferences.setIcecreamStatus(isChecked)
                unSubscribeToTopic(icecream.text.toString())
            }
        })

        diet.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                subscribeToTopic(diet.text.toString())
                SharedPreferences.setDietFoodStatus(isChecked)
            } else {
                SharedPreferences.setDietFoodStatus(isChecked)
                unSubscribeToTopic(icecream.text.toString())
            }
        })

    }

    private fun subscribeToTopic(category : String){
        val topic = "/topics/" + category
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
    }


    private fun unSubscribeToTopic(category : String){
        val topic = "/topics/" + category
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }
}
