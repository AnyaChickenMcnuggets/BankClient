package com.example.bankclient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME="PlanLibrary.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="plans";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_STATUS="plan_status";
    private static final String COLUMN_TITLE="plan_title";
    private static final String COLUMN_DATE="plan_date";
    private static final String COLUMN_RESPONSE="plan_response";

    private static final String IETABLE_NAME="incomes_expenses";
    private static final String IECOLUMN_SUM="ie_sum";
    private static final String IECOLUMN_TITLE="ie_title";
    private static final String IECOLUMN_LONG="ie_long";
    private static final String IECOLUMN_DATE="ie_date";
    private static final String IECOLUMN_INCOME="ie_income";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query=
                "CREATE TABLE " + TABLE_NAME+
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_DATE + " TEXT, " +
                        COLUMN_STATUS + " TEXT, " +
                        COLUMN_RESPONSE + " TEXT);";
        db.execSQL(query);
        query=
                "CREATE TABLE " + IETABLE_NAME+
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        IECOLUMN_SUM + " TEXT, " +
                        IECOLUMN_TITLE + " TEXT, " +
                        IECOLUMN_LONG + " TEXT, " +
                        IECOLUMN_DATE + " TEXT, " +
                        IECOLUMN_INCOME + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IETABLE_NAME);
        onCreate(db);
    }

    public void addPlan(String title, String date, String status, String response){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_STATUS, status);
        cv.put(COLUMN_RESPONSE, response);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result==-1){
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Успех", Toast.LENGTH_SHORT).show();
        }
    }

    public void addIE(String title, String sum, String date, Boolean isLong, Boolean isIncome){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(IECOLUMN_TITLE, title);
        cv.put(IECOLUMN_DATE, date);
        cv.put(IECOLUMN_SUM, sum);
        cv.put(IECOLUMN_LONG, isLong);
        cv.put(IECOLUMN_INCOME, isIncome);
        long result = db.insert(IETABLE_NAME, null, cv);
        if (result==-1){
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Успех", Toast.LENGTH_SHORT).show();
        }
    }
    public Cursor readAllLongIncome(){
        String query = "SELECT * FROM " + IETABLE_NAME +
                " WHERE " + IECOLUMN_INCOME + " = 'true' AND " + IECOLUMN_LONG + " = 'true'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor readAllShortIncome(){
        String query = "SELECT * FROM " + IETABLE_NAME +
                " WHERE " + IECOLUMN_INCOME + " = 'true' AND " + IECOLUMN_LONG + " = 'false'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor readAllLongExpense(){
        String query = "SELECT * FROM " + IETABLE_NAME +
                " WHERE " + IECOLUMN_INCOME + " = 'false' AND " + IECOLUMN_LONG + " = 'true'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor readAllShortExpense(){
        String query = "SELECT * FROM " + IETABLE_NAME +
                " WHERE " + IECOLUMN_INCOME + " = 'false' AND " + IECOLUMN_LONG + " = 'false'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllPlans(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public int countAllPlans(){
        String query = "SELECT COUNT (*) FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(query, null);
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int countNotUpdatedPlans() {
        String query = "SELECT COUNT (*) FROM " + TABLE_NAME + " WHERE " + COLUMN_RESPONSE + "= 'no response'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(query, null);
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        cursor.close();
        return count;
    }

    public void deleteOnePlan(String plan_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, COLUMN_ID + " = ? ", new String[]{plan_id});
        if (result==-1){
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Успех", Toast.LENGTH_SHORT).show();
        }
    }
}