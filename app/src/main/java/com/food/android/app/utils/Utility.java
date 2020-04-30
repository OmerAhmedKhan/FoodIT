package com.food.android.app.utils;
import android.app.ProgressDialog;
import android.content.Context;
import android.provider.Settings.Secure;

import androidx.annotation.StringRes;

/**
 * Created by Aleesha Kanwal on 9/25/2019.
 */
public class Utility {

    private static ProgressDialog progressDialog;

    public static String getDevceID(Context context){
        return  Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    public static void showProgressDialog(@StringRes int title, @StringRes int message, Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(context.getString(title));
        progressDialog.setMessage(context.getString(message));
        progressDialog.show();
    }

    public static void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }
}
