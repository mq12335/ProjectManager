package com.example.projectmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.projectmanager.R;

import com.example.projectmanager.viewModel.ProjectViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectDetailM extends AppCompatActivity {

    private ProjectViewModel viewModel;
    private String[] member = new String[]{};
    private String[] task = new String[]{};
    private String[] deadline = new String[]{};
    private String[] status = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail_m);


        ListView lv = findViewById(R.id.detaillv);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String porject = bundle.getString("project");

        runOnUiThread(() -> {
            viewModel = new ViewModelProvider(ProjectDetailM.this).get(ProjectViewModel.class);
            viewModel.getTasks(porject).observe(ProjectDetailM.this, new Observer<ArrayList<HashMap<String, String>>>() {
                @Override
                public void onChanged(ArrayList<HashMap<String, String>> hashMaps) {

                    List<String> arrMember = new ArrayList<String>();
                    arrMember.addAll(Arrays.asList(member));
                    List<String> arrStatus = new ArrayList<String>();
                    arrStatus.addAll(Arrays.asList(status));
                    List<String> arrTask = new ArrayList<String>();
                    arrTask.addAll(Arrays.asList(task));
                    List<String> arrDeadline = new ArrayList<String>();
                    arrDeadline.addAll(Arrays.asList(deadline));
                    List<Map<String, Object>> itemlist = new ArrayList<Map<String, Object>>();

                    for (int i = 0; i < hashMaps.size(); i++) {
                        arrMember.add(hashMaps.get(i).get("member"));
                        arrDeadline.add(hashMaps.get(i).get("ddl"));
                        System.out.println(hashMaps.get(i).get("ddl"));
                        arrStatus.add(hashMaps.get(i).get("status"));
                        arrTask.add(hashMaps.get(i).get("task"));
                        //System.out.println(hashMaps.get(i).get("task")+"  "+hashMaps.get(i).get("status"));
                    }
                    String[] outM = arrMember.toArray(new String[arrMember.size()]);
                    String[] outD = arrDeadline.toArray(new String[arrDeadline.size()]);
                    String[] outS = arrStatus.toArray(new String[arrStatus.size()]);
                    String[] outT = arrTask.toArray(new String[arrTask.size()]);

                    for (int i = 0; i < outM.length; i++) {
                        Map<String, Object> listitem = new HashMap<String, Object>();
                        listitem.put("task", "task: " + outT[i]);
                        listitem.put("member", "member:  " + outM[i]);
                        listitem.put("ddl", "deadline： " + outD[i]);
                        listitem.put("status", outS[i]);
                        itemlist.add(listitem);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(ProjectDetailM.this, itemlist,
                            R.layout.singlelv,
                            new String[]{"task", "member", "ddl", "status"},
                            new int[]{R.id.task, R.id.member, R.id.ddl, R.id.status});
                    lv.setAdapter(simpleAdapter);
                }
            });
        });

    }
}