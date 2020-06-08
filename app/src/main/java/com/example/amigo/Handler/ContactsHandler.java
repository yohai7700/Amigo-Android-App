package com.example.amigo.Handler;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ContactsHandler {
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
