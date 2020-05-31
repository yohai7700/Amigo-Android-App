package com.example.amigo.Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class PictureHandling {
    public static final int PICK_IMAGE_REQUEST = 1, MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 2,
            BITMAP_MAX_SIZE = 1000000;

    public static String getPicturePath(Context context,  Uri uri){
        //region get icon image by URI
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
        //endregion
    }

    public static void startGalleryForResult(Activity activity){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            //request the permission
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PictureHandling.MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
            /*  MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                app-defined int constant. The callback method gets the
                result of the request.*/
        else
            openGalleryForResult(activity);
    }

    public static void tryOpenGallery(Activity activity){
        if ((ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED))
            openGalleryForResult(activity);
    }

    private static void openGalleryForResult(Activity activity){
        Intent photoPick = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(photoPick, PictureHandling.PICK_IMAGE_REQUEST);
    }

    public static Bitmap getCompressedBitmap(Context context, Uri uri, int drawableDefault){
        try {
            return new Compressor(context).setMaxHeight(720).setMaxWidth(1280).compressToBitmap(new File(getPicturePath(context, uri)));
        } catch (IOException e) {
            e.printStackTrace();
            return BitmapFactory.decodeResource(context.getResources(), drawableDefault);
        }
    }
}