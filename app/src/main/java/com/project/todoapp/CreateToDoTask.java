package com.project.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;

public class CreateToDoTask extends AppCompatActivity {

    String user;
    EditText task_date, task_name, task_details;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_to_do_task);
        mContext = this;
        task_date = findViewById(R.id.et_date);
        task_name = findViewById(R.id.task_name);
        task_details = findViewById(R.id.task_details);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getString("username");
    }

    public void onClickSaveTask(View view) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        try {
            Tasks task = realm.createObject(Tasks.class, System.currentTimeMillis() / 1000);
            task.setUsername(user);
            task.setTask_name(task_name.getText().toString());
            task.setDate(task_date.getText().toString());
            task.setTask_details(task_details.getText().toString());
            realm.commitTransaction();
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            task_date.setText("");
            task_name.setText("");
            task_details.setText("");
        } catch (Exception ex) {
            realm.cancelTransaction();
            Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
        }
        startActivity(new Intent(this, ToDoTaskList.class));
    }

    public void onClickDiscardTask(View view){
        finish();
    }
}
