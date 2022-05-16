package com.example.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.projectmanager.R;
import com.example.projectmanager.viewModel.ProjectViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerList extends AppCompatActivity {

    private String[] projectName = new String[]{"Project name"};//这里之后改为从数据可以获取的数据存入
    private String[] status = new String[]{"Status"};


    private ProjectViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_list);

        Intent my = getIntent();
        Bundle bundle1 = my.getExtras();
        String user = bundle1.getString("User");
        ListView lv1 = (ListView) findViewById(R.id.lv1);



        Button createProject = (Button) findViewById(R.id.creat_project);
        Button createTask = (Button) findViewById(R.id.add_task);
        Button complete = findViewById(R.id.complete);
        Button ongoing = findViewById(R.id.onging);

        runOnUiThread(() -> {

            viewModel = new ViewModelProvider(ManagerList.this).get(ProjectViewModel.class);
            viewModel.getProjectsStatus(user.isEmpty()?"user1":user,ProjectViewModel.Direction.ASCENDING).observe(ManagerList.this,
                    new Observer<ArrayList<HashMap<String, String>>>() {
                @Override
                public void onChanged(ArrayList<HashMap<String, String>> hashMaps) {
                    List<String> arr = new ArrayList<String>();
                    arr.addAll(Arrays.asList(projectName));
                    List<String> arr2 = new ArrayList<String>();
                    arr2.addAll(Arrays.asList(status));

                    List<Map<String, Object>> itemlist = new ArrayList<Map<String, Object>>();

                    for (int i = 0; i < hashMaps.size(); i++) {
                        arr.add(hashMaps.get(i).get("name"));
                        arr2.add(hashMaps.get(i).get("status"));
                        System.out.println(arr.size());

                    }
                    String[] out = arr.toArray(new String[arr.size()]);
                    String[] out2 = arr2.toArray(new String[arr2.size()]);
                    // System.out.println(out2.length);
                    for (int i = 0; i < out.length; i++) {

                        Map<String, Object> listitem = new HashMap<String, Object>();
                        listitem.put("projectName", out[i]);
                        listitem.put("status", out2[i]);
                        itemlist.add(listitem);
                    }
                  /*  int j = 1;
                    for(int i = 1;i < itemlist.size();i++){
                        if(itemlist.get(i).get("status").equals("finished")){
                            //交换位置
                        }
                    }*/
                    SimpleAdapter simpleAdapter = new SimpleAdapter(ManagerList.this, itemlist,
                            R.layout.lv1_item,
                            new String[]{"projectName", "status"},
                            new int[]{R.id.projectName, R.id.project_status});
                    lv1.setAdapter(simpleAdapter);

                    lv1.setOnItemClickListener((adapterView, view, i, l) -> {
                        if (i != 0) {
                            System.out.println(out[i]);
                            Intent intent = new Intent(ManagerList.this, ProjectDetailM.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("project",out[i]);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });


                    lv1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            System.out.println(projectName[i] + "1214");

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            });
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

   /*     System.out.println("aaa"+arr.size());
        String[] out = arr.toArray(new String[arr.size()]);
        String[] out2 = arr2.toArray(new String[arr2.size()]);
       // System.out.println(out2.length);
        for (int i = 0; i < out.length; i++) {

            Map<String, Object> listitem = new HashMap<String, Object>();
            listitem.put("projectName", out[i]);
            listitem.put("status", out2[i]);
            itemlist.add(listitem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, itemlist,
                R.layout.lv1_item,
                new String[]{"projectName", "status"},
                new int[]{R.id.projectName, R.id.project_status});
        lv1.setAdapter(simpleAdapter);
        */


        createProject.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerList.this, CeateProject.class);
            Bundle b1 = new Bundle();
            b1.putString("User", user);
            intent.putExtras(b1);
            startActivity(intent);
            finish();

        });
        createTask.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerList.this, TaskCreate.class);
            Bundle b1 = new Bundle();
            b1.putString("User", user);
            intent.putExtras(b1);
            startActivity(intent);
            finish();
        });
        complete.setOnClickListener( v ->{
            Intent intent = new Intent(ManagerList.this, Filter.class);
            Bundle bundle = new Bundle();
            bundle.putString("status","complete");
            bundle.putString("User",user);
            intent.putExtras(bundle);
            startActivity(intent);

        });
        ongoing.setOnClickListener(v ->{
            Intent intent = new Intent(ManagerList.this, Filter.class);
            Bundle bundle = new Bundle();
            bundle.putString("status","on going");
            bundle.putString("User",user);
            intent.putExtras(bundle);
            startActivity(intent);
        });


    }
}