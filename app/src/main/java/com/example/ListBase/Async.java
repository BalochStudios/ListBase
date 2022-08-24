package com.example.ListBase;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class Async extends AsyncTask<String,Void,Long> {
    Context context;
    DbHelper dbHelper;
    long result;

    Async(Context ct) {
        context=ct;
        dbHelper = new DbHelper(ct);
    }
    @Override
    protected Long doInBackground(String... param) {

            String listName = param[0];
            ToDoList toDoList = new ToDoList();
            toDoList.setListName(listName);
            result = dbHelper.addData(toDoList);
            return result;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Long l) {
        Toast.makeText(context, "Data Inserted"+l, Toast.LENGTH_SHORT).show();
    }
}
