package com.example.amigo.Handler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ContactsHandler {
    public static String getContactName(Context context, Uri dataUri){
        Cursor cursor = context.getContentResolver().query(dataUri, null,null, null, null);
        cursor.moveToFirst();
        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        return cursor.getString(nameIndex);
    }

    public static Uri getContactPhotoUri(Context context, Uri dataUri){
        Cursor cursor = context.getContentResolver().query(dataUri, null,null, null, null);
        int uriIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
        return null;
    }

    public static void openContacts(Activity activity){
        Log.d("TAG", "Tried opening contacts");
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //request the permission
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS},
                    PermissionsHandler.MY_PERMISSIONS_READ_CONTACTS);
            Log.d("TAG", "Asked perm opening contacts");
        }
        tryOpenContacts(activity);
    }

    public static void tryOpenContacts(Activity activity){
        if ((ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED))
            openContactsForResult(activity);
    }

    private static void openContactsForResult(Activity activity){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        activity.startActivityForResult(contactPickerIntent, RequestHandler.READ_CONTACTS_REQUEST);
    }
}
