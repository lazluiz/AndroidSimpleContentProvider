package br.com.zelius.simplecontentprovider.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBContract {

    // It's just convenient to use our app package name as the Content Authority
    public static final String CONTENT_AUTHORITY = "br.com.zelius.simplecontentprovider";

    // URI used for other apps to contact our Content Provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths to get data from our Content Provider
    public static final String PATH_ALARMS = "alarms";

    /* Define your tables */
    public static final class AlarmsEntry implements BaseColumns {

        // content://br.com.zelius.simplecontentprovider/alarms
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ALARMS).build();

        // Get multiple results
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALARMS;

        // Get one result
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALARMS;

        public static final String TABLE_NAME = "alarms";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DATE_CREATED = "date_created";

        public static Uri buildURI(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getURIName(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static long getURIStartDate(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_DATE_CREATED);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }
    }
}
