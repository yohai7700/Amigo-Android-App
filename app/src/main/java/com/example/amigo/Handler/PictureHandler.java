package com.example.amigo.Handler;

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

import java.io.File;
import java.io.IOException;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import id.zelory.compressor.Compressor;

/**
 * @author Yohai Mazuz
 * This class handles all picture handling functions.
 */
public class PictureHandler {
    /**
     * This function returns picture file path from a URI.
     * @param context The context from which the function is called
     * @param uri The URI of teh picture
     * @return picture file path
     */
    public static String getPicturePath(Context context,  Uri uri){
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    /**
     * This functions open permission for gallery, if granted it opens the gallery for choosing a picture.
     * @param activity the activity to return result
     */
    public static void openGalleryPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            //request the permission
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PermissionsHandler.MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
        tryOpenGallery(activity);
    }
    /**
     * This functions tries to open gallery, depends if permission is granted.
     * @param activity the activity to return result
     */
    public static void tryOpenGallery(Activity activity){
        if ((ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED))
            openGalleryForResult(activity);
    }
    /**
     * This functions opens gallery for choosing a picture without checking
     * permissions.
     * @param activity the activity to return result
     */
    private static void openGalleryForResult(Activity activity){
        Intent photoPick = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(photoPick, RequestHandler.PICK_IMAGE_REQUEST);
    }

    /**
     * receives uri, returns compressed bitmap of the picture in the URI or default drawable if can't
     * get picture from URI.
     * @param context context from which function is called
     * @param uri uri of the picture
     * @param drawableDefault default resource drawble if can't compress
     * @return compressed bitmap of picture
     */
    public static Bitmap getCompressedBitmap(Context context, Uri uri, int drawableDefault){
        try {
            return new Compressor(context).setMaxHeight(720).setMaxWidth(1280).compressToBitmap(new File(getPicturePath(context, uri)));
        } catch (IOException e) {
            e.printStackTrace();
            return BitmapFactory.decodeResource(context.getResources(), drawableDefault);
        }
    }
}
