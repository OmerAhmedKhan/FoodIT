package com.food.android.app.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class SharedPreferences {

    private static android.content.SharedPreferences sharedPreferences;
    private static android.content.SharedPreferences.Editor editor;

    public SharedPreferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferences getPrefernces(Context context) {
        return new SharedPreferences(context);
    }

    public static void setUserName(String value) {
        editor.putString("userName", value);
        editor.apply();
    }

    public static String getUserName() {
        return sharedPreferences.getString("userName", "");
    }

    public static boolean isLogin() {
        return sharedPreferences.getBoolean("loginStatus", false);
    }

    public static void setLoginStatus(boolean status) {
        editor.putBoolean("loginStatus", status);
        editor.apply();
    }

    public static void setFirstTimeList(boolean status) {
        editor.putBoolean("firstTimeList", status);
        editor.apply();
    }

    public static boolean isFirstTimeList() {
        return sharedPreferences.getBoolean("firstTimeList", false);
    }

    public static boolean isBBQ() {
        return sharedPreferences.getBoolean("BBQ", false);
    }

    public static boolean isDesi() {
        return sharedPreferences.getBoolean("Desi", false);
    }

    public static boolean isMocktails() {
        return sharedPreferences.getBoolean("Mocktails", false);
    }

    public static boolean isChineese() {
        return sharedPreferences.getBoolean("Chineese", false);
    }

    public static boolean isDesserts() {
        return sharedPreferences.getBoolean("Desserts", false);
    }

    public static boolean isSteaks() {
        return sharedPreferences.getBoolean("Steaks", false);
    }

    public static boolean isSeaFood() {
        return sharedPreferences.getBoolean("SeaFood", false);
    }

    public static boolean isIcecream() {
        return sharedPreferences.getBoolean("Icecream", false);
    }

    public static boolean isDietFood() {
        return sharedPreferences.getBoolean("DietFood", false);
    }

    public static void setBBQStatus(boolean status) {
        editor.putBoolean("BBQ", status);
        editor.apply();
    }

    public static void setDesiStatus(boolean status) {
        editor.putBoolean("Desi", status);
        editor.apply();
    }

    public static void setMocktailsStatus(boolean status) {
        editor.putBoolean("Mocktails", status);
        editor.apply();
    }

    public static void setChineeseStatus(boolean status) {
        editor.putBoolean("Chineese", status);
        editor.apply();
    }

    public static void setDessertsStatus(boolean status) {
        editor.putBoolean("Desserts", status);
        editor.apply();
    }

    public static void setSteaksStatus(boolean status) {
        editor.putBoolean("Steaks", status);
        editor.apply();
    }

    public static void setSeaFoodStatus(boolean status) {
        editor.putBoolean("SeaFood", status);
        editor.apply();
    }

    public static void setDietFoodStatus(boolean status) {
        editor.putBoolean("DietFood", status);
        editor.apply();
    }

    public static void setIcecreamStatus(boolean status) {
        editor.putBoolean("Icecream", status);
        editor.apply();
    }

  /*  public static boolean isLogin() {
        return sharedPreferences.getBoolean("loginStatus", false);
    }

    public static void setLoginStatus(boolean status) {
        editor.putBoolean("loginStatus", status);
        editor.apply();
    }
*/
    public static void clearUserData() {
        editor.putString("userName", "");
        editor.putBoolean("BBQ", false);
        editor.putBoolean("Desi", false);
        editor.putBoolean("Mocktails", false);
        editor.putBoolean("Chineese", false);
        editor.putBoolean("Desserts", false);
        editor.putBoolean("Steaks", false);
        editor.putBoolean("SeaFood", false);
        editor.putBoolean("DietFood", false);
        editor.putBoolean("Icecream", false);
        editor.putBoolean("loginStatus", false);
        editor.apply();
        sharedPreferences.edit().clear().apply();
    }

/*    public static void clearUserData() {
        editor.putString("email", "");
        editor.putString("loggedInWIth", "");
        editor.putString("userName", "");
        editor.putString("userId", "");
        editor.putBoolean("loginStatus", false);
        editor.apply();

        sharedPreferences.edit().clear().apply();
    }*/


}
