package com.example.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.projectmanager.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.example.projectmanager.R;
import com.example.projectmanager.model.Task;
import com.example.projectmanager.model.User;
import com.example.projectmanager.viewModel.ProjectViewModel;
import com.example.projectmanager.viewModel.UserViewModel;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class TaskCreate extends AppCompatActivity {

    private ProjectViewModel viewModel;
    private UserViewModel viewModel1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String user = bundle.getString("User");


        viewModel = new ViewModelProvider(TaskCreate.this).get(ProjectViewModel.class);
        viewModel1 = new ViewModelProvider(TaskCreate.this).get(UserViewModel.class);

        EditText project = findViewById(R.id.add_taskp_name);
        EditText add = findViewById(R.id.add_task_name);
        EditText membername = findViewById(R.id.task_member_name);
        EditText dd = findViewById(R.id.TDD);
        EditText mm = findViewById(R.id.TMM);
        EditText yyyy = findViewById(R.id.TYYYY);



        Button submit = findViewById(R.id.create_task_submit);
        Button finfish1 = findViewById(R.id.finish);


        submit.setOnClickListener( v ->{
            String ddl = dd.getText().toString() + mm.getText().toString() + yyyy.getText().toString();
            System.out.println("aa"+ddl);
            viewModel.addTask(add.getText().toString(), project.getText().toString(),membername.getText().toString(),ddl);
            Intent my = getIntent();
            Bundle bundle1 = my.getExtras();
            String username = bundle1.getString("User");
            viewModel1.getUserByName(username, new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    return null;
                }
            }, new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    return null;
                }
            }).observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if(user.getNotification()=="") {
                        viewModel1.updateUserNotification(membername.getText().toString(), "You are assigned to " + add.getText().toString(), new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                return null;
                            }
                        }, new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                return null;
                            }
                        });
                    } else {
                        String new_notification = user.getNotification() + "\n" + "You are assigned to " + add.getText().toString();
                        viewModel1.updateUserNotification(membername.getText().toString(), new_notification, new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                return null;
                            }
                        }, new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                return null;
                            }
                        });
                    }
                }
            });


        });

        finfish1.setOnClickListener(v ->{
            Intent intent1 = new Intent(TaskCreate.this, ManagerList.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("User",user);
            intent1.putExtras(bundle1);
            startActivity(intent1);
            finish();
        });

    }
}