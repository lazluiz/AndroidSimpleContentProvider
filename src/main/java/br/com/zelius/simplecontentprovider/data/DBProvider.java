package br.com.zelius.simplecontentprovider.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class DBProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper mDBHelper;
    private ContentResolver mContentResolver;

    static final int ALARM = 100;
    static final int ALARM_WITH_NAME = 101;
    static final int ALARM_WITH_DATE = 102;

    // URI Matcher
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = DBContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, DBContract.PATH_ALARMS, ALARM);
        matcher.addURI(authority, DBContract.PATH_ALARMS + "/*", ALARM_WITH_NAME);
        matcher.addURI(authority, DBContract.PATH_ALARMS + "/#", ALARM_WITH_DATE);

        return matcher;
    }

    // Load our Database and ContentResolver
    @Override
    public boolean onCreate() {
        if(getContext() != null){
            mDBHelper = new DBHelper(getContext());
            mContentResolver = getContext().getContentResolver();
        }

        return true;
    }

    // Here we match the possible URIs
    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ALARM:
            case ALARM_WITH_DATE:
                return DBContract.AlarmsEntry.CONTENT_TYPE;
            case ALARM_WITH_NAME:
                return DBContract.AlarmsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case ALARM:
                cursor = db.query(
                        DBContract.AlarmsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ALARM_WITH_NAME:
                String name = DBContract.AlarmsEntry.getURIName(uri);

                cursor = db.query(
                        DBContract.AlarmsEntry.TABLE_NAME,
                        projection,
                        DBContract.AlarmsEntry.COLUMN_NAME + " = ? ",
                        new String[]{name},
                        null,
                        null,
                        sortOrder
                );
                break;
            case ALARM_WITH_DATE:
                long date = DBContract.AlarmsEntry.getURIStartDate(uri);

                cursor = db.query(
                        DBContract.AlarmsEntry.TABLE_NAME,
                        projection,
                        DBContract.AlarmsEntry.COLUMN_DATE_CREATED + " >= ? ",
                        new String[]{Long.toString(date)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(mContentResolver, uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ALARM: {
                long _id = db.insert(DBContract.AlarmsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DBContract.AlarmsEntry.buildURI(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        mContentResolver.notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int deletedRows;

        // If null, we put selection as '1' so it will return the number of rows deleted
        if(selection == null) selection = "1";

        switch (match) {
            case ALARM: {
                deletedRows = db.delete(DBContract.AlarmsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        // Only notify if something was actually deleted
        if (deletedRows != 0)
            mContentResolver.notifyChange(uri, null);

        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int updatedRows;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ALARM: {
                updatedRows = db.update(DBContract.AlarmsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (updatedRows != 0)
            mContentResolver.notifyChange(uri, null);

        return updatedRows;
    }
}
