package com.gmail.pavkascool.hometask3.contacts.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import com.gmail.pavkascool.hometask3.R;

import java.io.File;
import java.io.IOException;

import static android.content.pm.PackageManager.FEATURE_CAMERA;

public class CameraUtils {

    public static final String FILENAME_PREFIX = "JPEG_PHOTO_CONTACTS_";
    public static boolean hasCameraHardware(@NonNull Context context) {
        return context.getPackageManager().hasSystemFeature(FEATURE_CAMERA);
    }

    public static File preparePhotoFile(@NonNull Context context, long id) throws IOException {
        String imageFileName = FILENAME_PREFIX + id + ".jpg";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(storageDir, imageFileName);
        return file;
    }

    private CameraUtils() {
    }
}
