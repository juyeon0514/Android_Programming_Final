package com.example.scheduler.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Currency;

public class AlaramReminderContract
{

    public static final String CONTENT_AUTOMORITY = "com.delaroystudios.alaramreminder";
    public  static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTOMORITY);

    public  static final String PATH_VEHICLE = "reminder-path";

    public  static final class AlarmReminderEntry implements BaseColumns{

         public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VEHICLE);

         final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTOMORITY + "/" + PATH_VEHICLE;

        public static final String CONTENT_ITEM_TYPE =
                 ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTOMORITY + "/" + PATH_VEHICLE;

        public final static String TABLE_NAME = "vehicles";

        public final static String _ID = BaseColumns._ID;

        public static final String KEY_TITLE = "title";
        public static final String KEY_DATE = "date";
        public static final String KEY_TIME = "time";
        public static final String KEY_REPEAT = "repeat";
        public static final String KEY_ACTIVE = "active";
        public static final String KEY_TAG = "tag";
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1) {
            return cursor.getString(columnIndex);
        } else {
            return null;
        }
    }
}
