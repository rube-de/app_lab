package de.ruf2.dailyselfie;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.Map;
import java.util.Set;

import de.ruf2.dailyselfie.data.SelfieDbHelper;
import de.ruf2.dailyselfie.data.SelfieContract.SelfieEntry;

/**
 * Created by Bernhard Ruf on 11.12.2014.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(SelfieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new SelfieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }


    public void testInsertReadDb() {
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        SelfieDbHelper dbHelper = new SelfieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = createSelfieValues();

        long locationRowId;
        locationRowId = db.insert(SelfieEntry.TABLE_NAME, null, values);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                SelfieEntry.TABLE_NAME,  // Table to Query
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        validateCursor(cursor,values);

        dbHelper.close();
    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }

    static ContentValues createSelfieValues(){
        // Test data we're going to insert into the DB to see if it works.
        String testName = "test1";
        String testDateText = "2014-04-01";
        String testPath = "/test/path";
        String testThumb = "/test/thumb";


        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(SelfieEntry.COLUMN_NAME, testName);
        values.put(SelfieEntry.COLUMN_DATETEXT, testDateText);
        values.put(SelfieEntry.COLUMN_PATH, testPath);
        values.put(SelfieEntry.COLUMN_THUMB, testThumb);
        return values;
    }
}
