package de.ruf2.dailyselfie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import de.ruf2.dailyselfie.data.SelfieContract.SelfieEntry;
import de.ruf2.dailyselfie.data.SelfieContract.AlbumEntry;

/**
 * Created by Bernhard Ruf on 11.12.2014.
 */
public class SelfieDbHelper extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "selfie.db";

    public SelfieDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public SelfieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_SELFIE_TABLE = "CREATE TABLE " + SelfieEntry.TABLE_NAME + "("+
                SelfieEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SelfieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                SelfieEntry.COLUMN_DATETEXT + " TEXT NOT NULL, " +
                SelfieEntry.COLUMN_PATH + " TEXT NOT NULL, " +
                SelfieEntry.COLUMN_THUMB + " TEXT, " +
                SelfieEntry.COLUMN_ALBUM_KEY + " INTEGER ";
        db.execSQL(SQL_CREATE_SELFIE_TABLE);

        final String SQL_CREATE_ALBUM_TABLE = "CREATE TABLE " + AlbumEntry.TABLE_NAME + "("+
                AlbumEntry._ID + "INTEGER PRIMARY KEY, " +
                AlbumEntry.COLUMN_NAME + " TEXT NOT NULL ";
        db.execSQL(SQL_CREATE_ALBUM_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //TODO: alternate on live version
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SelfieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AlbumEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
