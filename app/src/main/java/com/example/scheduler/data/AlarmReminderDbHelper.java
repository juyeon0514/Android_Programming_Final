package com.example.scheduler.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmReminderDbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "alarmreminder.db";

    private static final int DATABASE_VERSION = 1;

    public AlarmReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sqlListDatabase){
        String SQL_CREATE_ALARAM_TABLE = "CREATE TABLE " + AlaramReminderContract.AlarmReminderEntry.TABLE_NAME + " ("
                +AlaramReminderContract.AlarmReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +AlaramReminderContract.AlarmReminderEntry.KEY_TITLE + " TEXT NOT NULL, "
                +AlaramReminderContract.AlarmReminderEntry.KEY_DATE + " TEXT NOT NULL, "
                +AlaramReminderContract.AlarmReminderEntry.KEY_TIME + " TEXT NOT NULL, "
                +AlaramReminderContract.AlarmReminderEntry.KEY_REPEAT + " TEXT NOT NULL, "
                +AlaramReminderContract.AlarmReminderEntry.KEY_ACTIVE + " TEXT NOT NULL, "
                +AlaramReminderContract.AlarmReminderEntry.KEY_TAG + " TEXT NOT NULL " + ");";
            sqlListDatabase.execSQL(SQL_CREATE_ALARAM_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스를 업그레이드할 때 테이블을 삭제하고 새로 생성합니다.
        db.execSQL("DROP TABLE IF EXISTS " + AlaramReminderContract.AlarmReminderEntry.TABLE_NAME);
        onCreate(db);
    }
}
