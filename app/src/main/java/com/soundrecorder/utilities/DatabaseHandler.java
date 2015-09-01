package com.soundrecorder.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.soundrecorder.beans.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BLUEHORSE 123 on 8/28/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_SIZE = "size";
    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DURATION + " TEXT," + KEY_SIZE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addItem(Items items) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, items.getRecord_name());
        values.put(KEY_DURATION, items.getDuration());
        values.put(KEY_SIZE, items.getSize());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection

        Log.e("DATABASE: ","Successfully Created");
    }

    // Getting single contact
    public Items getOneItems(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_DURATION, KEY_SIZE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Items items = new Items(cursor.getString(0),cursor.getString(1),cursor.getInt(3));
        // return contact
        return items;
    }

    // Getting All Contacts
    public List<Items> getItems() {
        List<Items> Lst = new ArrayList<Items>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Items items = new Items();
                items.setIndex(Integer.parseInt(cursor.getString(0)));
                items.setRecord_name(cursor.getString(1));
                items.setDuration(cursor.getString(2));
                items.setSize(Integer.parseInt(cursor.getString(3)));
                // Adding contact to list
                Lst.add(items);
            } while (cursor.moveToNext());
        }

        // return contact list
        return Lst;
    }

    // Getting contacts Count
    public int getItemCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating single contact
    public int updateItems(Items items) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, items.getRecord_name());
        values.put(KEY_DURATION, items.getDuration());
        values.put(KEY_DURATION, items.getSize());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(items.getIndex()) });
    }

    // Deleting single contact
    public void deleteItem(Items items) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(items.getIndex()) });
        db.close();
    }

    public void deleteItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS,null,null);
        db.close();
    }
}
