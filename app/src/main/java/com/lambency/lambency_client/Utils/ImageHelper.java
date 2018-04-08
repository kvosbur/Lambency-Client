package com.lambency.lambency_client.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
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

    public static String saveImage(Context context, String image, final String fileName){
        File outputDir = context.getCacheDir();
        try {
            File[] matchedFiles = outputDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith(fileName);
                }
            });
            if(matchedFiles.length != 0){
                return matchedFiles[0].getPath();
            }
            File outputFile = File.createTempFile(fileName, null, outputDir);

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

        /*
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .asBitmap()
                .load(imagePath)
                .into(view);
                */
        imagePath = imagePath.replace("photos", "");

        imagePath = imagePath.replace(" ", "%20");

        System.out.println("Image Path is: " + imagePath);

        Glide.with(context)
                .load(LambencyAPIHelper.domain + imagePath)
                .into(view);
    }

    public static void displayEasyImageResult(Context context, File imageFile, ImageView imageView){
        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        Glide.with(context).load(myBitmap).into(imageView);
    }

    public static byte[] getByteArrayFromPath(Context context, String imagePath){
        Bitmap bm;
        final byte[] imageFile;
        if (imagePath.equals("")) {
            //Use default profile image
            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_default_avatar);
            imageFile = null;
        } else {
            bm = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageFile = baos.toByteArray();
        }

        return imageFile;
    }

}
