package com.lambency.lambency_client.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lshan on 2/21/2018.
 */

public class ImageHelper {
    public static Bitmap stringToBitmap(String image){
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static String saveImage(Context context, String image, String fileName){
        File outputDir = context.getCacheDir();
        try {

            File outputFile = File.createTempFile(fileName, null, outputDir);
            outputFile.deleteOnExit();
            FileOutputStream out = new FileOutputStream(outputFile);
            Bitmap bitmap = stringToBitmap(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return outputFile.getPath();

        }catch (IOException e){
            e.printStackTrace();
        }

        //Should never get this
        return null;
    }

    public static void loadWithGlide(Context context, String imagePath, ImageView view){
        RequestOptions requestOptions = new RequestOptions();

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .asBitmap()
                .load(imagePath)
                .into(view);
    }

}
