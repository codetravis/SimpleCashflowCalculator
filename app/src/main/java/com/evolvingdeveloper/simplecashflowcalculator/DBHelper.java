package com.evolvingdeveloper.simplecashflowcalculator;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;


public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SimpleCashFlowCalculatorDB.db";
    public static final String PROPERTY_TABLE_NAME = "property";
    public static final String PROPERTY_COLUMN_ID = "_id";
    public static final String PROPERTY_COLUMN_PROPERTY_VALUE = "property_value";
    public static final String PROPERTY_COLUMN_NAME = "name";
    public static final String PROPERTY_COLUMN_ESTIMATED_RENT = "estimated_rent";
    public static final String PROPERTY_COLUMN_REPAIR = "repair";
    public static final String PROPERTY_COLUMN_VACANCY = "vacancy";
    public static final String PROPERTY_COLUMN_MORTGAGE = "mortgage";
    public static final String PROPERTY_COLUMN_INSURANCE = "insurance";
    public static final String PROPERTY_COLUMN_PROPERTY_TAX = "property_tax";
    public static final String PROPERTY_COLUMN_PROPERTY_MANAGEMENT = "property_management";

    public static final String PROPERTY_TABLE_CREATE =
            "CREATE TABLE " + PROPERTY_TABLE_NAME + " ( " +
            PROPERTY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PROPERTY_COLUMN_NAME +" TEXT, " +
            PROPERTY_COLUMN_PROPERTY_VALUE + " TEXT, " +
            PROPERTY_COLUMN_ESTIMATED_RENT + " TEXT, " +
            PROPERTY_COLUMN_REPAIR + " TEXT, " +
            PROPERTY_COLUMN_VACANCY + " TEXT, " +
            PROPERTY_COLUMN_MORTGAGE + " TEXT, " +
            PROPERTY_COLUMN_INSURANCE + " TEXT, " +
            PROPERTY_COLUMN_PROPERTY_TAX + " TEXT, " +
            PROPERTY_COLUMN_PROPERTY_MANAGEMENT + " TEXT)";

    DBHelper (Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(PROPERTY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PROPERTY_TABLE_NAME);
        onCreate(db);
    }

    public long insertProperty(String name, String property_value, String estimated_rent, String repair,
                                  String vacancy, String mortgage, String insurance, String property_tax,
                                  String property_management) {
        long new_id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PROPERTY_COLUMN_NAME, name);
        contentValues.put(PROPERTY_COLUMN_PROPERTY_VALUE, property_value);
        contentValues.put(PROPERTY_COLUMN_ESTIMATED_RENT, estimated_rent);
        contentValues.put(PROPERTY_COLUMN_REPAIR, repair);
        contentValues.put(PROPERTY_COLUMN_VACANCY, vacancy);
        contentValues.put(PROPERTY_COLUMN_MORTGAGE, mortgage);
        contentValues.put(PROPERTY_COLUMN_INSURANCE, insurance);
        contentValues.put(PROPERTY_COLUMN_PROPERTY_TAX, property_tax);
        contentValues.put(PROPERTY_COLUMN_PROPERTY_MANAGEMENT, property_management);

        new_id = db.insert(PROPERTY_TABLE_NAME, null, contentValues);

        return new_id;
    }

    public int updateProperty(long id, String name, String property_value, String estimated_rent, String repair,
                               String vacancy, String mortgage, String insurance, String property_tax,
                               String property_management) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PROPERTY_COLUMN_NAME, name);
        contentValues.put(PROPERTY_COLUMN_PROPERTY_VALUE, property_value);
        contentValues.put(PROPERTY_COLUMN_ESTIMATED_RENT, estimated_rent);
        contentValues.put(PROPERTY_COLUMN_REPAIR, repair);
        contentValues.put(PROPERTY_COLUMN_VACANCY, vacancy);
        contentValues.put(PROPERTY_COLUMN_MORTGAGE, mortgage);
        contentValues.put(PROPERTY_COLUMN_INSURANCE, insurance);
        contentValues.put(PROPERTY_COLUMN_PROPERTY_TAX, property_tax);
        contentValues.put(PROPERTY_COLUMN_PROPERTY_MANAGEMENT, property_management);

        return db.update(PROPERTY_TABLE_NAME, contentValues, "_id = " + id, null);
    }

    public Cursor getProperty(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from property where _id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PROPERTY_TABLE_NAME);
        return numRows;
    }

    public Cursor getAllProperties() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + PROPERTY_TABLE_NAME, null );
        return res;
    }

}
