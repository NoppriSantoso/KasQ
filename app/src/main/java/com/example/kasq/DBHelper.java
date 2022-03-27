package com.example.kasq;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;

import com.example.kasq.Model.Transaksi;
import com.example.kasq.Model.User;
import com.example.kasq.UserDAO.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper implements UserDAO {
    private Context context;
    private static final String TABLE_USER = "users";
    private static final String TABLE_TRANSACTION = "transaksidetails";
    private static final String CREATE_TABLE_USER=" CREATE TABLE " + TABLE_USER + "(" +
            "userId CHAR(5) PRIMARY KEY," +
            "userName VARCHAR(20) NOT NULL," +
            "userPassword VARCHAR(30) NOT NULL" +
            ")";
    private static final String CREATE_TABLE_TRANSACTION=" CREATE TABLE " + TABLE_TRANSACTION + "(" +
            "id CHAR(5) PRIMARY KEY," +
            "date DATE NOT NULL," +
            "category VARCHAR(20) NOT NULL," +
            "value INT NOT NULL," +
            "type VARCHAR(20) NOT NULL,"+
            "description TEXT NOT NULL," +
            "UserID CHAR(5) NOT NULL," +
            "FOREIGN KEY(UserId) REFERENCES " + TABLE_USER + " (userId) " +
            ")";

    public DBHelper(Context context) {
        super(context, "Kasq.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(CREATE_TABLE_TRANSACTION);
        DB.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists "+TABLE_TRANSACTION+"");
        DB.execSQL(" DROP TABLE IF EXISTS "+ TABLE_USER+ "");
    }

    @Override
    public Boolean registerUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId",user.getUserId());
        values.put("userName",user.getUserName());
        values.put("userPassword",user.getUserPassword());

        long result = db.insert(TABLE_USER,null,values);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public User loginUser(String username, String password) {
        User user = null;
        String query = " SELECT * FROM " + TABLE_USER + " WHERE userName = '" + username + "' AND userPassword = '" + password + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        try{
            if(cursor.moveToFirst()){
                String userId, userName, userPassword;
                userId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
                userName = cursor.getString(cursor.getColumnIndexOrThrow("userName"));
                userPassword = cursor.getString(cursor.getColumnIndexOrThrow("userPassword"));
                user = new User(userId, userName, userPassword);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return user;
    }

    public Boolean addTransaksi(Transaksi transaksi){
        SQLiteDatabase DB = getWritableDatabase();
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put("id",transaksi.getId());
            contentValues.put("date",transaksi.getDate());
            contentValues.put("type",transaksi.getType());
            contentValues.put("category",transaksi.getCategory());
            contentValues.put("value",transaksi.getValue());
            contentValues.put("description",transaksi.getDescription());
            contentValues.put("userId",transaksi.getUser().getUserId());
            long result = DB.insert(TABLE_TRANSACTION,null,contentValues);
            if(result == -1){
                return false;
            }else{
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if( DB!=null && DB.inTransaction()){
                DB.endTransaction();
            }
        }
        return false;
    }


    public User getUser(String userId){
        String query = "SELECT * FROM " + TABLE_USER + " WHERE UserID = '" + userId + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User user = null;
        try{
            if(cursor.moveToFirst()){
                String userName, userPassword;
                userId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
                userName = cursor.getString(cursor.getColumnIndexOrThrow("userName"));
                userPassword = cursor.getString(cursor.getColumnIndexOrThrow("userPassword"));
                user = new User(userId, userName,userPassword);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.isClosed();
            }
            if(db != null && db.inTransaction()){
                db.endTransaction();
            }
        }
        return user;
    }


    public Cursor getData(String userId){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select * from " + TABLE_TRANSACTION + " Where UserID = '"+ userId +"'",null);
        return cursor;
    }
    public double getPemasukanData(String userId){
        double sum = 0;
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select sum(value) as Total from transaksiDetails Where type = 'Pemasukan' AND UserID = '"+ userId +"'",null);
        if(cursor.moveToFirst()){
            sum = cursor.getDouble(0);
        }
        return sum;
    }
    public double getPengeluaranData(String userId){
        double sum = 0;
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select sum(value) as Total from transaksiDetails Where type = 'Pengeluaran' AND UserID = '"+ userId +"'",null);
        if(cursor.moveToFirst()){
            sum = cursor.getDouble(0);
        }
        return sum;
    }


}
