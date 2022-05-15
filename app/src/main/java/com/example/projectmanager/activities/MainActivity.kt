package com.example.projectmanager.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanager.R
import com.example.projectmanager.adapter.TableViewAdapter
import com.example.projectmanager.viewModel.ProjectViewModel
import com.example.projectmanager.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ProjectViewModel
    private lateinit var viewModel1: UserViewModel
    private lateinit var projectList: ArrayList<HashMap<String, String>>
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        viewModel1 = ViewModelProvider(this).get(UserViewModel::class.java)
       // getcurrentUser()
        val intent = getIntent()
        val bundle = intent.extras
        val username = bundle?.getString("User")
        println(username)
        if (username != null) {
            viewModel.getProjectsStatus(username).observe(this) { projects ->
                recyclerViewProjectList.layoutManager = LinearLayoutManager(this)
                recyclerViewProjectList.adapter = TableViewAdapter(projects)
            }
        }

    }

//    private fun getcurrentUser() {
//        username = viewModel1.getCurrentUserName()
//    }

}