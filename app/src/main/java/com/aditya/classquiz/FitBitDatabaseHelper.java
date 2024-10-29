package com.aditya.classquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FitBitDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="FitBitDatabase";
    private static final int DB_VERSION=1;
    private static final String USER_TABLE="users";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_MOBILE = "mobile";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_BMI = "bmi";
    private static final String COLUMN_STEPS = "steps_taken";

    public FitBitDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE="CREATE TABLE "+USER_TABLE+"("+
                COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_NAME+" TEXT,"+
                COLUMN_AGE+" INTEGER,"+
                COLUMN_MOBILE+" TEXT,"+
                COLUMN_GENDER+" TEXT,"+
                COLUMN_BMI+" REAL,"+
                COLUMN_STEPS+" INTEGER)";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);
        onCreate(db);
    }

    public long addUser(String username,int age,String mobile,String gender){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues user=new ContentValues();
        user.put(COLUMN_NAME,username);
        user.put(COLUMN_AGE,age);
        user.put(COLUMN_MOBILE,mobile);
        user.put(COLUMN_GENDER,gender);
        user.put(COLUMN_BMI,0.0);
        user.put(COLUMN_STEPS,0);
        long result=db.insert(USER_TABLE,null,user);
        db.close();
        return result;
    }

    public boolean updateUserBmi(long id,float bmi){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_BMI,bmi);
        int num_rows=db.update(USER_TABLE,cv,COLUMN_ID+" =?",new String[]{String.valueOf((int)id)});
        db.close();
        return num_rows>0;
    }

    public boolean updateUserStepCount(long id,int num_steps){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_STEPS,num_steps);
        int num_rows=db.update(USER_TABLE,cv,COLUMN_ID+" =?",new String[]{String.valueOf((int)id)});
        db.close();
        return num_rows>0;
    }

    public Cursor getUserById(long id){
        SQLiteDatabase db=this.getReadableDatabase();
        String query= "SELECT * FROM "+USER_TABLE+" WHERE "+COLUMN_ID+" =?";
        Cursor cursor=db.rawQuery(query,new String[]{String.valueOf((int)id)});
        return cursor;
    }

    public Cursor getUserIds(){
        SQLiteDatabase db=this.getReadableDatabase();
        String query= "SELECT "+COLUMN_ID+" FROM "+USER_TABLE;
        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    public int getUserStepCount(long id){
        int user_step_count=0;
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT "+COLUMN_STEPS+" FROM "+USER_TABLE+" WHERE "+COLUMN_ID+" =?";
        Cursor cursor=db.rawQuery(query,new String[]{String.valueOf((int)id)});
        if(cursor.moveToFirst())
            user_step_count=cursor.getInt(cursor.getColumnIndexOrThrow("steps_taken"));
        return user_step_count;
    }

    public boolean resetStepCount(){
        Cursor user_ids=this.getUserIds();
        if(user_ids.moveToFirst()){
            do{
                long user_id=user_ids.getLong(user_ids.getColumnIndexOrThrow("id"));
                updateUserStepCount(user_id,0);
            }while(user_ids.moveToNext());
            return true;
        }
        return false;
    }

}
