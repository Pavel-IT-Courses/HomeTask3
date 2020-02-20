package com.gmail.pavkascool.hometask3.contacts.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import java.io.File;

public class ImageUtils {
    public static Bitmap decodeBitmapFromFile(File imageFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        System.out.println("Image File = " + imageFile.getAbsolutePath() + " exists = " + imageFile.exists());
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
    }

    private ImageUtils() {
    }
}
