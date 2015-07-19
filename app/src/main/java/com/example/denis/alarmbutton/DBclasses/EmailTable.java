package com.example.denis.alarmbutton.DBclasses;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * AlarmButton created by Denis Pavlovsky on 07.05.15.
 */
public class EmailTable {

    public static final String TABLE_NAME = "mails";

    public static final String COLUMN_TEXT = "adress";


    public static void createTable(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_TEXT + " text"
                + ");");
    }
}
