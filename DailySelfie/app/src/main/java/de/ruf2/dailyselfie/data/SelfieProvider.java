package de.ruf2.dailyselfie.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Bernhard Ruf on 11.12.2014.
 */
public class SelfieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SelfieDbHelper mOpenHelper;

    private static final int SELFIE = 100;
    private static final int SELFIE_WITH_ALBUM = 101;
    private static final int ALBUM = 300;
    private static final int ALBUM_ID = 301;

    private static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SelfieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, SelfieContract.PATH_SELFIE, SELFIE);
        matcher.addURI(authority, SelfieContract.PATH_SELFIE + "/*", SELFIE_WITH_ALBUM);
        matcher.addURI(authority, SelfieContract.PATH_ALBUM, ALBUM);
        matcher.addURI(authority, SelfieContract.PATH_ALBUM + "/#", ALBUM_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new SelfieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case SELFIE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SelfieContract.SelfieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SELFIE:
                return SelfieContract.SelfieEntry.CONTENT_TYPE;
            case SELFIE_WITH_ALBUM:
                return SelfieContract.SelfieEntry.CONTENT_ITEM_TYPE;
        }

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
