package com.car.toll_car.Model.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static android.accounts.AccountManager.KEY_PASSWORD;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ETC_User.db";
    // User table name
    private static final String TABLE_USER = "user_sign_up";
    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_MOBILE = "user_mobile";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_MOBILE + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_USER);
    }

    int c=0;

    public void AddUser(String mobile, String password){
        c=1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_USER_STATUS, status);
        values.put(COLUMN_USER_MOBILE, mobile);
        values.put(COLUMN_USER_PASSWORD, password);
        long check= db.insert(TABLE_USER, null, values);
    }

    public boolean isEmailExists(String mobile) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{COLUMN_USER_ID, COLUMN_USER_MOBILE, COLUMN_USER_PASSWORD},
                COLUMN_USER_MOBILE + "=?", new String[]{mobile}, null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }
        //if email does not exist return false
        return false;
    }

    public Cursor DisplayData(){
        SQLiteDatabase sqLiteDatabase= getReadableDatabase();
        Cursor cursor= sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_USER, null);
        return cursor;
    }

}
