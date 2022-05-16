package com.example.projectmanager.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.R
import com.example.projectmanager.viewModel.ProjectViewModel
import com.example.projectmanager.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.tv_home
import kotlinx.android.synthetic.main.activity_user_profile.*

class MainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var viewModel: ProjectViewModel
    private lateinit var viewModel1: UserViewModel
    private lateinit var projectList: ArrayList<HashMap<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        viewModel1 = ViewModelProvider(this).get(UserViewModel::class.java)
        //val project_list = findViewById<RecyclerView>(R.id.recyclerViewProjectList) as RecyclerView
        val project_list_show = findViewById<View>(R.id.recyclerViewProjectList) as ListView


       // getcurrentUser()
        val intent = getIntent()
        val bundle = intent.extras
        val username = bundle?.getString("User")
        println(username)
        if (username != null) {
            viewModel1.getUserByName(username, {}, {}).observe(this) { user1 ->
                if(user1.notification!="") {
                    Toast.makeText(
                        this@MainActivity,
                        user1.notification,
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel1.updateUserNotification(username, "", {}, {})
                }
            }
            viewModel.getProjectsStatus(username).observe(this) { projects ->
                println(projects)
                var bundle = Bundle()
                bundle.putString("User", username)
                val simpleAdapter = SimpleAdapter(
                    this@MainActivity,
                    projects,
                    R.layout.table_list_item,
                    arrayOf("name", "status"),
                    intArrayOf(R.id.ProjectName, R.id.Status)
                )
                project_list_show.adapter = simpleAdapter
                project_list_show.onItemClickListener =
                    OnItemClickListener { _: AdapterView<*>?, _: View?, i: Int, _: Long ->
                            println(projects[i])
                            val intent = Intent(this@MainActivity, ProjectDetailsActivity::class.java)
                            bundle.putString("Project", projects[i]["name"])
                            intent.putExtras(bundle)
                            startActivity(intent)
                    }
            }
        }

        tv_home.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.recyclerViewProjectList -> {
                    println(projectList[R.id.recyclerViewProjectList])
                }
            }
        }
    }

}