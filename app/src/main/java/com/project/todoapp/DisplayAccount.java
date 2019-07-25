package com.project.todoapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class DisplayAccount extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_display_activity);
        final TextView name = findViewById(R.id.name);
        final TextView username = findViewById(R.id.username);
        final TextView pswd = findViewById(R.id.pswd);

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String key = getIntent().getExtras().getString("username");
                User user = realm.where(User.class).equalTo("username",key).findFirst();
                name.setText(name.getText().toString() + user.getName());
                username.setText(username.getText().toString() + user.getUsername());
                pswd.setText(pswd.getText().toString() + user.getPassword());
            }
        });
    }
}
