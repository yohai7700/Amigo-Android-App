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

/**
 * @author Yohai Mazuz
 * This class handles Contacts functions for other classes.
 */
public class ContactsHandler {

    /**
     * @param context The context of calling the function
     * @param dataUri The uri of the contact as given by using an Action Intent
     * @return returns the contact name
     */
    public static String getContactName(Context context, Uri dataUri){
        Cursor cursor = context.getContentResolver().query(dataUri, null,null, null, null);
        cursor.moveToFirst();
        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        return cursor.getString(nameIndex);
    }

    /**
     * This function is currently not used. It returns URI of a contact picture
     * @param context The context of calling the function
     * @param dataUri The uri of the contact as given by using an Action Intent
     * @return Uri of the contact picture
     */
    public static Uri getContactPhotoUri(Context context, Uri dataUri){
        Cursor cursor = context.getContentResolver().query(dataUri, null,null, null, null);
        cursor.moveToFirst();
        int uriIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
        return null;
    }
    /**
     * This function opens the contacts window to choose a contact. returns result to given activity.
     * @param activity the activity to return the result
     */
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

    /**
     * This functions tries to open contacts, if permission is granted already the opens contacts.
     * @param activity the activity to return the result
     */
    public static void tryOpenContacts(Activity activity){
        if ((ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED))
            openContactsForResult(activity);
    }

    /**
     * This function opens contacts for result without checking permissions.
     * @param activity the activity to return the result
     */
    private static void openContactsForResult(Activity activity){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        activity.startActivityForResult(contactPickerIntent, RequestHandler.READ_CONTACTS_REQUEST);
    }
}
