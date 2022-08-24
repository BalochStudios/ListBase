package com.example.ListBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {


    private static final String TABLE_LISTS = "ToDoList";
    private static final String KEY_LISTS_ID = "listId";
    private static final String KEY_LISTS_NAME = "listName";

    private static final String CREATE_TABLE_LIST = "CREATE TABLE " + TABLE_LISTS + "( " +
            KEY_LISTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_LISTS_NAME + " TEXT"+
            " )";


    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    public DbHelper(@Nullable Context context) {
        super(context, "list.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LIST);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);
        onCreate(db);

    }

    public long addData(ToDoList toDoList) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_LISTS_NAME, toDoList.getListName());
        SQLiteDatabase db = getWritableDatabase();
        return  db.insert(TABLE_LISTS, null, cv);


    }


    public int deleteData(ToDoList toDoList) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_LISTS, KEY_LISTS_ID + " = " + toDoList.getListId(), null);
    }

    public int updateData(ToDoList toDoList) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_LISTS_NAME, toDoList.getListName());
        SQLiteDatabase db = getWritableDatabase();
        return db.update(TABLE_LISTS, cv, KEY_LISTS_ID + "=" + toDoList.getListId(),null);
        //toDoList.getListId()
    }
    public int getData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + KEY_LISTS_ID + " FROM " + TABLE_LISTS, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
   public ArrayList<ToDoList> getToDoList() {
        ArrayList<ToDoList> toDoListss = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LISTS + " ORDER By " + KEY_LISTS_ID, null);
       //database starts with some entries in it every time it gets created
       ToDoList toDoList1 = new ToDoList(1, "item 1");
       ToDoList toDoList2 = new ToDoList(2, "item 2");
       ToDoList toDoList3 = new ToDoList(3, "item 3");
       toDoListss.add(toDoList1);
       toDoListss.add(toDoList2);
       toDoListss.add(toDoList3);
           while (cursor.moveToNext()) {
               @SuppressLint("Range") int listId = cursor.getInt(cursor.getColumnIndex(KEY_LISTS_ID));
               @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(KEY_LISTS_NAME));
               ToDoList toDoList = new ToDoList(listId, name);
               toDoListss.add(toDoList);
           }
        return toDoListss;
    }
    public int clearData() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_LISTS, "1", null);

    }
    public boolean isProductInList(ToDoList toDoList) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= db.rawQuery("select * FROM " + TABLE_LISTS + " WHERE " + KEY_LISTS_ID + "=?", new String[]{ String.valueOf(toDoList.getListId())});
        int count = cursor.getCount();
        cursor.close();
        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

}
