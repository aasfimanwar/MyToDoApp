package com.project.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ToDoTaskList extends AppCompatActivity {
    RecyclerView mRecyclerView;
    String user;
    RealmResults<Tasks> userTasks;

    NavigationView nv;
    DrawerLayout dl;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_task_list);

        dl = findViewById(R.id.drawerLayout);
        nv = findViewById(R.id.navigationView);

        toggle = new ActionBarDrawerToggle(this,dl,R.string.open_menu,R.string.close_menu);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.account:
                        Intent intent = new Intent(ToDoTaskList.this, DisplayAccount.class);
                        String key = getIntent().getExtras().getString("username");
                        intent.putExtra("username", key);
                        startActivity(intent);
                        break;
                    case R.id.exit:
                        finish();
                        moveTaskToBack(true);
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        Bundle bundle=getIntent().getExtras();
        user=bundle.getString("username");
        mRecyclerView=findViewById(R.id.task_recycler);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm realm=Realm.getDefaultInstance();
        userTasks=realm.where(Tasks.class).equalTo("username",user).findAll();
        callAdapter();
    }
    public void fragment(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.add(R.id.empty_container,new NoTaskFragment());
        ft.commit();
    }
    public void onClickCreateTask(View view){
        Intent intent = new Intent(this,CreateToDoTask.class);
        intent.putExtra("username",user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void markAllDone(View view) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(int i=0;i<userTasks.size();i++)
                    userTasks.get(i).setCheck_task(true);
                callAdapter();
            }
        });
    }

    public void callAdapter(){
        Toast.makeText(this, Integer.toString(userTasks.size()), Toast.LENGTH_SHORT).show();
        if(userTasks.size()==0)
            fragment();
        else {
            MyAdapter myAdapter=new MyAdapter(userTasks,this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(myAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
