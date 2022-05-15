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

public class Filter extends AppCompatActivity {


    private String[] projectName = new String[]{"Project name"};//这里之后改为从数据可以获取的数据存入
    private String[] status = new String[]{"Status"};


    private ProjectViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String porject = bundle.getString("status");
        String user = bundle.getString("User");

        List<String> arr = new ArrayList<String>();
        arr.addAll(Arrays.asList(projectName));
        List<String> arr2 = new ArrayList<String>();
        arr2.addAll(Arrays.asList(status));


        ListView lv2 = (ListView) findViewById(R.id.lv2);
        List<Map<String, Object>> itemlist = new ArrayList<Map<String, Object>>();
        Button  back = findViewById(R.id.back);

        runOnUiThread(() -> {
            viewModel = new ViewModelProvider(Filter.this).get(ProjectViewModel.class);
            viewModel.getProjectsStatus(user, ProjectViewModel.Direction.ASCENDING).observe(Filter.this,
                    new Observer<ArrayList<HashMap<String, String>>>() {
                        @Override
                        public void onChanged(ArrayList<HashMap<String, String>> hashMaps) {
                            for (int i = 0; i < hashMaps.size(); i++) {
                                arr.add(hashMaps.get(i).get("name"));
                                arr2.add(hashMaps.get(i).get("status"));
                                System.out.println(arr.size());

                            }
                            String[] out = arr.toArray(new String[arr.size()]);
                            String[] out2 = arr2.toArray(new String[arr2.size()]);
                            // System.out.println(out2.length);
                            for (int i = 0; i < out.length; i++) {


                                if (out2[i] .equals(porject)) {
                                    Map<String, Object> listitem = new HashMap<String, Object>();
                                    listitem.put("projectName", out[i]);
                                    listitem.put("status", out2[i]);
                                    itemlist.add(listitem);
                                }
                            }
                  /*  int j = 1;
                    for(int i = 1;i < itemlist.size();i++){
                        if(itemlist.get(i).get("status").equals("finished")){
                            //交换位置
                        }
                    }*/
                            SimpleAdapter simpleAdapter = new SimpleAdapter(Filter.this, itemlist,
                                    R.layout.lv1_item,
                                    new String[]{"projectName", "status"},
                                    new int[]{R.id.projectName, R.id.project_status});
                            lv2.setAdapter(simpleAdapter);


                        }
                    });
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        back.setOnClickListener( v -> {
            finish();
        });
    }
}