package com.food.android.app.utils;

/**
 * Created by Aleesha Kanwal on 9/19/2019.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.food.android.app.App;

import java.io.ByteArrayOutputStream;

public class ImageUtil
{
    public static Bitmap convertToBitmap(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convertBase64String(Bitmap bitmap)
    {


        BitmapFactory.Options options;
        options = new BitmapFactory.Options();
        options.inSampleSize = 6;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

         bitmap.compress(Bitmap.CompressFormat.JPEG, 12, outputStream);

        Log.e("base64",Base64.encodeToString(outputStream.toByteArray(), 0));

        String path = MediaStore.Images.Media.insertImage(App.getAppContext().getContentResolver(),bitmap,"Title",null);
        Log.e("base64-2",path);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_PADDING);
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
