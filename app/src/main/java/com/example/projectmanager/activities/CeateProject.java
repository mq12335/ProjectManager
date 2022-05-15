package com.example.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.projectmanager.R;
import com.example.projectmanager.viewModel.ProjectViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class CeateProject extends AppCompatActivity {

    private LinearLayout layout;
    private int i = -1;
    private ArrayList<EditText> taskNames = new ArrayList<>();
    private EditText taskName;
    private ArrayList<EditText> taskMemberNames = new ArrayList<>();
    private EditText taskMemberName;
    private ArrayList<EditText> taskDeadlines = new ArrayList<>();
    private EditText taskDeadline;
    private TextView numberId;
    private ArrayList<TextView> numberIds = new ArrayList<>();

    // *
    private ProjectViewModel viewModel;

    public static void actionStart(Context context, String name) {
        Intent intent = new Intent(context, CeateProject.class);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }
    // *


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceate_project);

        viewModel = new ViewModelProvider(CeateProject.this).get(ProjectViewModel.class);
        // viewModel = new ViewModelProvider(ManagerList.this).get(ProjectViewModel.class);

        EditText createProjectName = (EditText) findViewById(R.id.create_project_name);
        EditText description = (EditText) findViewById(R.id.project_description);
//        Button addTaskButton = findViewById(R.id.add_task);
//        Button deleteTaskButton = findViewById(R.id.delete_task);
        Button submitProjectButton = findViewById(R.id.create_project_submit);
        EditText dd = findViewById(R.id.DD);
        EditText mm = findViewById(R.id.MM);
        EditText yyyy = findViewById(R.id.YYYY);



//        viewModel = new ViewModelProvider(this).get(ProjectViewModel.class);// *
//        viewModel.getProjectsStatus("user1").observe(this, new Observer<ArrayList<HashMap<String, String>>>() {
//            @Override
//            public void onChanged(ArrayList<HashMap<String, String>> hashMaps) {
//               for (int i = 0 ; i < hashMaps.size();i++)
//                   System.out.println(hashMaps.get(i).get("name"));
//            }
//        });

//        addTaskButton.setOnClickListener(v -> {
//            addTask();
//        });
//        deleteTaskButton.setOnClickListener(v -> {
//            deleteTask();
//        });
        submitProjectButton.setOnClickListener(v -> {
            String ddl = dd.getText().toString() + mm.getText().toString() + yyyy.getText().toString();
            submitProject(createProjectName.getText().toString(), description.getText().toString(), ddl); // *

        });

    }

//    @SuppressLint("SetTextI18n")
//    public void addTask() {
//        layout = findViewById(R.id.task_layout);
//
//        i++;
//        //number ID
//        numberId = new TextView(this);
//        numberId.setText("this  is     " + (i) + "    task: ");
//        numberIds.add(numberId);
//        layout.addView(numberId);
//        //Task name
//        taskName = new EditText(this);
//        taskName.setHint("Task name ");
//        taskNames.add(taskName);
//        layout.addView(taskName);
//        //Member name
//        taskMemberName = new EditText(this);
//        taskMemberName.setHint("Team member name");
//        taskMemberNames.add(taskMemberName);
//        layout.addView(taskMemberName);
//        //Deadline
//        taskDeadline = new EditText(this);
//        taskDeadline.setHint("DDMMYYYY");
//        taskDeadlines.add(taskDeadline);
//        layout.addView(taskDeadline);
//        System.out.println(taskDeadlines.get(i));
//
//    }

//    public void deleteTask() {
//        System.out.println("bbb" + i);
//        layout = findViewById(R.id.task_layout);
//        layout.removeView(taskDeadlines.get(i));
//        System.out.println(taskDeadlines.get(i));
//        layout.removeView(taskMemberNames.get(i));
//        layout.removeView(taskNames.get(i));
//        layout.removeView(numberIds.get(i));
//        i--;
//        System.out.println("ccc" + i);
//    }

    public void submitProject(String createProjectName, String projectDescription, String ddl) {

        viewModel.addProject(createProjectName, "user1", projectDescription, ddl);
        Intent intent = new Intent(CeateProject.this, ManagerList.class);
        startActivity(intent);
        finish();


    }
}