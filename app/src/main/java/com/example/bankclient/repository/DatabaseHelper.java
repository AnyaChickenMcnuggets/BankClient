package com.example.bankclient.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.bankclient.ui.models.Plan;
import com.example.bankclient.ui.models.UsedBankProduct;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME="PlanLibrary.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="plans";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_STATUS="plan_status";
    private static final String COLUMN_SUM="plan_sum";
    private static final String COLUMN_TITLE="plan_title";
    private static final String COLUMN_DATE="plan_date";
    private static final String COLUMN_RESPONSE="plan_response";
    private static final String COLUMN_STARTPLOT="plan_plot";
    private static final String COLUMN_SOLUTION="plan_solution";


    private static final String PIETABLE_NAME="plans_incomes_expenses";
    private static final String PIECOLUMN_PLAN="plan_id";
    private static final String PIECOLUMN_INCOME_EXPENSE="ie_id";

    private static final String PUBPTABLE_NAME="plans_used_bank_products";
    private static final String PUBPCOLUMN_PLAN="plan_id";
    private static final String PUBPCOLUMN_USED_BANK_PRODUCT="ubp_id";

    private static final String IETABLE_NAME="incomes_expenses";
    private static final String IECOLUMN_SUM="ie_sum";
    private static final String IECOLUMN_TITLE="ie_title";
    private static final String IECOLUMN_LONG="ie_long";
    private static final String IECOLUMN_DATE="ie_date";
    private static final String IECOLUMN_INCOME="ie_income";
    private static final String IECOLUMN_PERIOD="ie_period";

    private static final String BPTABLE_NAME="bank_products";
    private static final String BPCOLUMN_TITLE="bp_title";
    private static final String BPCOLUMN_DATE="bp_date";
    private static final String BPCOLUMN_PERCENTAGE="bp_percentage";
    private static final String BPCOLUMN_INCOME="bp_income";
    private static final String BPCOLUMN_CREDITLIMIT="bp_limit";
    private static final String BPCOLUMN_MINPAY="bp_minpay";

    private static final String UBPTABLE_NAME="used_bank_products";
    private static final String UBPCOLUMN_TITLE="ubp_title";
    private static final String UBPCOLUMN_DATE="ubp_date";
    private static final String UBPCOLUMN_PERCENTAGE="ubp_percentage";
    private static final String UBPCOLUMN_INCOME="ubp_income";
    private static final String UBPCOLUMN_SUM="ubp_sum";
    private static final String UBPCOLUMN_START="ubp_start";
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
                        COLUMN_SUM + " TEXT, " +
                        COLUMN_STATUS + " TEXT, " +
                        COLUMN_RESPONSE + " TEXT, " +
                        COLUMN_STARTPLOT + " TEXT, " +
                        COLUMN_SOLUTION + " TEXT);";
        db.execSQL(query);
        query=
                "CREATE TABLE " + IETABLE_NAME+
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        IECOLUMN_TITLE + " TEXT, " +
                        IECOLUMN_SUM + " TEXT, " +
                        IECOLUMN_DATE + " TEXT, " +
                        IECOLUMN_LONG + " TEXT, " +
                        IECOLUMN_INCOME + " TEXT, " +
                        IECOLUMN_PERIOD + " TEXT);";
        db.execSQL(query);
        query=
                "CREATE TABLE " + BPTABLE_NAME+
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        BPCOLUMN_TITLE + " TEXT, " +
                        BPCOLUMN_DATE + " TEXT, " +
                        BPCOLUMN_PERCENTAGE + " TEXT, " +
                        BPCOLUMN_INCOME + " TEXT, " +
                        BPCOLUMN_CREDITLIMIT + " TEXT, " +
                        BPCOLUMN_MINPAY + " TEXT);";
        db.execSQL(query);
        query=
                "CREATE TABLE " + UBPTABLE_NAME+
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        UBPCOLUMN_TITLE + " TEXT, " +
                        UBPCOLUMN_DATE + " TEXT, " +
                        UBPCOLUMN_PERCENTAGE + " TEXT, " +
                        UBPCOLUMN_INCOME + " TEXT, " +
                        UBPCOLUMN_SUM + " TEXT, " +
                        UBPCOLUMN_START + " TEXT);";
        db.execSQL(query);
        query=
                "CREATE TABLE " + PIETABLE_NAME+
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PIECOLUMN_PLAN + " INTEGER REFERENCES " + TABLE_NAME +"("+ COLUMN_ID +"),"+
                        PIECOLUMN_INCOME_EXPENSE + " INTEGER REFERENCES " + IETABLE_NAME +"("+ COLUMN_ID +"));";
        db.execSQL(query);
        query=
                "CREATE TABLE " + PUBPTABLE_NAME+
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PUBPCOLUMN_PLAN + " INTEGER REFERENCES " + TABLE_NAME +"("+ COLUMN_ID +"),"+
                        PUBPCOLUMN_USED_BANK_PRODUCT + " INTEGER REFERENCES " + UBPTABLE_NAME +"("+ COLUMN_ID +"));";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IETABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BPTABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UBPTABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PUBPTABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PIETABLE_NAME);
        onCreate(db);
    }
    public void addStartProduct(String title, String date, String percentage, String income){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(BPCOLUMN_TITLE, title);
        cv.put(BPCOLUMN_DATE, date);
        cv.put(BPCOLUMN_PERCENTAGE, percentage);
        cv.put(BPCOLUMN_INCOME, income);
        cv.put(BPCOLUMN_CREDITLIMIT, income);
        cv.put(BPCOLUMN_MINPAY, income);
        long result = db.insert(BPTABLE_NAME, null, cv);
        if (result==-1){
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    public long addPlan(String title,
                        String date,
                        String sum,
                        String[] id_ies,
                        ArrayList<UsedBankProduct> ubps,
                        String status,
                        String response,
                        String plot){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues plan_cv = new ContentValues();

        plan_cv.put(COLUMN_TITLE, title);
        plan_cv.put(COLUMN_DATE, date);
        plan_cv.put(COLUMN_SUM, sum);
        plan_cv.put(COLUMN_STATUS, status);
        plan_cv.put(COLUMN_RESPONSE, response);
        plan_cv.put(COLUMN_STARTPLOT, plot);
        plan_cv.put(COLUMN_SOLUTION, "");
        long plan_cv_id = db.insert(TABLE_NAME, null, plan_cv);
        if (plan_cv_id==-1){
            Toast.makeText(context, "Ошибка ПЛАН", Toast.LENGTH_SHORT).show();
        }

        // добавить все используемые банковские продукты
        for (UsedBankProduct ubp:
                ubps) {
            ContentValues ubpcv = new ContentValues();

            ubpcv.put(UBPCOLUMN_TITLE, ubp.getTitle());
            ubpcv.put(UBPCOLUMN_DATE, ubp.getTime());
            ubpcv.put(UBPCOLUMN_PERCENTAGE, ubp.getPercentage());
            ubpcv.put(UBPCOLUMN_INCOME, ubp.getIncome() ? "true" : "false");
            ubpcv.put(UBPCOLUMN_SUM, ubp.getSum());
            ubpcv.put(UBPCOLUMN_START, ubp.getStartDate());
            long ubp_id = db.insert(UBPTABLE_NAME, null, ubpcv);
            if (ubp_id==-1){
                Toast.makeText(context, "Ошибка ПРОДУКТ", Toast.LENGTH_SHORT).show();
            }

            ContentValues pubpcv = new ContentValues();

            pubpcv.put(PUBPCOLUMN_PLAN, String.valueOf(plan_cv_id));
            pubpcv.put(PUBPCOLUMN_USED_BANK_PRODUCT, String.valueOf(ubp_id));
            long pubp_id = db.insert(PUBPTABLE_NAME, null, pubpcv);
            if (pubp_id==-1){
                Toast.makeText(context, "Ошибка ПРОДУКТ ПЛАН", Toast.LENGTH_SHORT).show();
            }

        }

        for (String id_ie:
                id_ies) {
            ContentValues piecv = new ContentValues();

            piecv.put(PIECOLUMN_PLAN, String.valueOf(plan_cv_id));
            piecv.put(PIECOLUMN_INCOME_EXPENSE, id_ie);
            long pubp_id = db.insert(PIETABLE_NAME, null, piecv);
            if (pubp_id==-1){
                Toast.makeText(context, "Ошибка ИЕ ПЛАН", Toast.LENGTH_SHORT).show();
            }
        }
        return plan_cv_id;
    }

    public Cursor getPlanById(String id){
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID + " = '" + id+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public void addIE(String title, String sum, String date, String isLong, String isIncome, String period){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(IECOLUMN_TITLE, title);
        cv.put(IECOLUMN_DATE, date);
        cv.put(IECOLUMN_SUM, sum);
        cv.put(IECOLUMN_LONG, isLong);
        cv.put(IECOLUMN_INCOME, isIncome);
        cv.put(IECOLUMN_PERIOD, period);

        long result = db.insert(IETABLE_NAME, null, cv);
        if (result==-1){
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Успех", Toast.LENGTH_SHORT).show();
        }
    }
    public Cursor readAllBankIncome(){
        String query = "SELECT * FROM " + BPTABLE_NAME +
                " WHERE " + BPCOLUMN_INCOME + " = 'true'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor readAllBankExpense(){
        String query = "SELECT * FROM " + BPTABLE_NAME +
                " WHERE " + BPCOLUMN_INCOME + " = 'false'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor readBankByTitle(String title){
        String query = "SELECT * FROM " + BPTABLE_NAME +
                " WHERE " + BPCOLUMN_TITLE + " = '" + title + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor readAllBank(){
        String query = "SELECT * FROM " + BPTABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor readIEById(String id){
        String query = "SELECT * FROM " + IETABLE_NAME +
                " WHERE " + COLUMN_ID + " = '" + id+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
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
        }
    }

    public void deleteIE(String ie_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(IETABLE_NAME, COLUMN_ID + " = ? ", new String[]{ie_id});
        if (result==-1){
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }
    public void editIE(String ie_id, String title, String sum, String date, String isLong, String isIncome, String period){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(IECOLUMN_TITLE, title);
        cv.put(IECOLUMN_DATE, date);
        cv.put(IECOLUMN_SUM, sum);
        cv.put(IECOLUMN_LONG, isLong);
        cv.put(IECOLUMN_INCOME, isIncome);
        cv.put(IECOLUMN_PERIOD, period);

        long result = db.update(IETABLE_NAME, cv, "_id=?", new String[]{ie_id});
        if (result==-1){
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    public void addSolutionById(Plan plan, String plot, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, plan.getTitle());
        cv.put(COLUMN_DATE, plan.getDate());
        cv.put(COLUMN_SUM, plan.getSum());
        cv.put(COLUMN_STATUS, plan.getStatus());
        cv.put(COLUMN_RESPONSE, plan.getResponse());
        cv.put(COLUMN_STARTPLOT, plan.getPlot());
        cv.put(COLUMN_SOLUTION, plot);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{id});
        if (result==-1){
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }

}
