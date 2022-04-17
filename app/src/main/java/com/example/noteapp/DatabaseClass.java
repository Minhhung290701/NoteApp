package com.example.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseClass extends SQLiteOpenHelper {

    //Khai báo tên các biến final
    Context context;
    private static final String DatabaseName = "MyNotes.db";
    private static final int DatabaseVersion = 1;

    private static final String TableName = "mynotes";
    private static final String ColumnId = "id";
    private static final String ColumnTitle = "title";
    private static final String ColumnDescription = "description";

    //Tạo DatabaseClass
    public DatabaseClass(@Nullable Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        this.context = context;
    }

    //Method conCreate gọi ra khi Activity được chạy
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Khai báo câu lệnh để tạo mới table
        String query = "CREATE TABLE " + TableName +
                " (" + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColumnTitle + " TEXT, " +
                ColumnDescription + " TEXT);";
        //Sử dụng execSQL để chạy câu lệnh query
        db.execSQL(query);
    }

    //Void onUpgrade sẽ drop table nếu có và tạo mới db
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }

    //Void addNotes để thêm note mới vào db
    public void addNotes(String title, String description){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnDescription, description);

        long resultValue = db.insert(TableName, null, cv);

        if (resultValue == -1) {
            Toast.makeText(context, "Data Not Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Data Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    //Void readAllData trả về một curson đọc câu lệnh query để lấy mọi dữ liệu từ db
    Cursor readAllData() {
        String query = "SELECT * FROM " + TableName;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    //Xóa table trong db
    void deleteAllNotes() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM " + TableName;
        database.execSQL(query);
    }

    //UpdateNote
    void updateNotes(String title, String description, String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnDescription, description);

        long result = database.update(TableName, cv, "id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
        }

    }

    //Xóa một dòng trong db bằng id
    public void deleteSingleItem(String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(TableName, "id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Item Not Deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
