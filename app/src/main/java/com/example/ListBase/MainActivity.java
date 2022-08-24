package com.example.ListBase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    private Button btnAdd;
    private Button btnDelete;
    private EditText etAdd;
    private ArrayList<ToDoList> list;
    private ArrayList<ToDoList> dataList;
    private ListAdapter listAdapter;
    private RecyclerView rvList;
    private Async async;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.btnAdd);
        etAdd = findViewById(R.id.etAddList);
        btnDelete = findViewById(R.id.btnDelete);
        rvList = findViewById(R.id.rvList);
        dbHelper = new DbHelper(MainActivity.this);
        async = new Async(MainActivity.this);
        rvList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        list = new ArrayList<>();
        dataList = new ArrayList<>();
        Button crashButton = new Button(this);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });
        crashButton.setVisibility(View.GONE);

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // Delete Done button will delete any item in the list that starts with the Done
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getListName().contains("Done")) {
                           ToDoList toDoList = list.get(i);
                        int deleteId = dbHelper.deleteData(toDoList);
                        if (deleteId > 0) {
                            list.remove(i);
                            listAdapter.notifyItemRemoved(i);
                        } else {
                            Toast.makeText(MainActivity.this, "unable  to Delete Due to not Done ", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }

                }

            }
        });
    }

    @Override
    protected void onResume() {
        //open and read your DB in your onResume() method
        // add item to a list
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 saveData();
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });
        fetchList();
        super.onResume();
    }

    private void fetchList() {
        //database starts with some entries in it
        list = dbHelper.getToDoList();
         listAdapter=new ListAdapter(list, new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 //if the item does not already have a Done add this to the item and go to end
                 ToDoList toDoList = list.get(i);
                 if (! toDoList.getListName().contains("Done")) {
                     int deleteId = dbHelper.deleteData(toDoList);
                     if (deleteId > 0) {
                         list.remove(i);
                         listAdapter.notifyItemRemoved(i);
                     }
                         toDoList.setListName("Done "+toDoList.getListName());
                         long insertedId = dbHelper.addData(toDoList);
                         if (insertedId > -1) {
                             etAdd.setText("");
                             listAdapter.notifyDataSetChanged();
                             Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();

                         }
                     fetchList();

                 } else{
                     int deleteId = dbHelper.deleteData(toDoList);
                     if (deleteId > 0) {
                         list.remove(i);
                         listAdapter.notifyItemRemoved(i);
                     }

                     toDoList.setListName(toDoList.getListName().replace("Done","").trim());
                     list.add(0,toDoList);
                     int updateId = dbHelper.updateData(toDoList);
                     listAdapter.notifyDataSetChanged();
                     Toast.makeText(MainActivity.this, "Remove Done", Toast.LENGTH_SHORT).show();

                 }
             }
         });
         listAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
             @Override
             public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                 ToDoList toDoList = list.get(i);
                 AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(MainActivity.this);
                 deleteBuilder.setTitle("Confirmation")
                         .setMessage("Are you sure to delete it")
                         .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 int deleteId = dbHelper.deleteData(toDoList);
                                 if (deleteId > 0) {
                                     list.remove(toDoList);
                                     listAdapter.notifyItemRemoved(i);
                                 } else {
                                     Toast.makeText(MainActivity.this, "unable  to remove", Toast.LENGTH_SHORT).show();
                                 }
                             }
                         })
                         .setNegativeButton("no", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {

                             }
                         })
                         .show();

                 return true;
             }
         });
         rvList.setAdapter(listAdapter);

    }

    private void saveData() {
        String name = etAdd.getText().toString().trim();
        if (name.isEmpty()) {
            etAdd.setText("Please enter Name");
        } else {
            ToDoList toDoList = new ToDoList();
            toDoList.setListName(name);
            async.execute(name);
            etAdd.setText("");
            listAdapter.notifyDataSetChanged();
            fetchList();

        }
    }
    //no need to close the database . it will automatically close when it is not needed

}

