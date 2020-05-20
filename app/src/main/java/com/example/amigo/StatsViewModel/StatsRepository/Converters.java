package com.example.amigo.StatsViewModel.StatsRepository;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import androidx.room.TypeConverter;


public class Converters {

    // convert from bitmap to byte array
    @TypeConverter
    public static byte[] getBytes(Bitmap bitmap) {
        if(bitmap == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    @TypeConverter
    public static Bitmap getImage(byte[] image){
        if(image == null)
            return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
