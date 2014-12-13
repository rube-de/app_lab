package de.ruf2.dailyselfie.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bernhard Ruf on 11.12.2014.
 */
public class SelfieContract {

    public static final String CONTENT_AUTHORITY = "de.ruf2.dailyselfie";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final  String PATH_SELFIE = "selfie";
    public static final String PATH_ALBUM = "album";

    /*
/* Inner class that defines the table contents of the selfie table */
    public static final class SelfieEntry implements BaseColumns {

        public static final String TABLE_NAME = "selfie";
        //own given name
        public static final String COLUMN_NAME = "name";
        // Date, stored as Text with format yyyy-MM-dd
        public static final String COLUMN_DATETEXT = "date";
        // Path to the thumbnail picture
        public static final String COLUMN_THUMB = "thumb";
        // Path to the real picture
        public static final String COLUMN_PATH = "path";

        public static final String COLUMN_ALBUM_KEY = "album_id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SELFIE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_SELFIE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_SELFIE;

        public static Uri buildSelfieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class AlbumEntry implements BaseColumns{
        public static final String TABLE_NAME = "album";
        //album name
        public static final String COLUMN_NAME = "name";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ALBUM).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ALBUM;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ALBUM;

        public static Uri buildAlbumUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
