package com.example.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.projectmanager.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.example.projectmanager.R;
import com.example.projectmanager.viewModel.ProjectViewModel;

public class TaskCreate extends AppCompatActivity {

    private ProjectViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

        viewModel = new ViewModelProvider(TaskCreate.this).get(ProjectViewModel.class);

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

        });

        finfish1.setOnClickListener(v ->{

            finish();
        });

    }
}