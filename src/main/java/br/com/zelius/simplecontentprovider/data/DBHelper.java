package br.com.zelius.simplecontentprovider.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.zelius.simplecontentprovider.data.DBContract.AlarmsEntry;

public class DBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "simplecontentprovider.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + AlarmsEntry.TABLE_NAME + " (" +
                AlarmsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                AlarmsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                AlarmsEntry.COLUMN_TIME + " TIMESTAMP NOT NULL, " +
                AlarmsEntry.COLUMN_DATE_CREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AlarmsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}