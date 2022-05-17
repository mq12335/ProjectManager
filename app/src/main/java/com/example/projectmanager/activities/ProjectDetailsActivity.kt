package com.example.projectmanager.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.R
import com.example.projectmanager.dialog.CustomDialog
import com.example.projectmanager.viewModel.ProjectViewModel
import com.example.projectmanager.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_project_details.*
import kotlinx.android.synthetic.main.activity_project_details.tv_home
import kotlinx.android.synthetic.main.activity_register.*

class ProjectDetailsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var viewModel: ProjectViewModel
    private lateinit var viewModel1: UserViewModel
    private var builderForCustom: CustomDialog.Builder? = null
    private var mDialog: CustomDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_details)

        builderForCustom = CustomDialog.Builder(this)

        viewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        viewModel1 = ViewModelProvider(this).get(UserViewModel::class.java)
        val intent = getIntent()
        val bundle = intent.extras
        val username = bundle?.getString("User")
        val projectname = bundle?.getString("Project")
        println(projectname)
        val task_list_show = findViewById<View>(R.id.outstanding_tasks_list) as ListView
        if (username != null) {
            if (projectname != null) {
                viewModel.getProjectsByName(projectname).observe(this) { projects ->
                    var itemlist = projects["members"] as MutableList<String>
                    var memberList = ""
                    println(itemlist)
                    for(i in 0 until itemlist.size) {
                        if(memberList=="")
                            memberList=itemlist[i]
                        else
                            memberList = memberList+", "+itemlist[i]
                    }
                    tv_manager_name.text = projects["manager"] as CharSequence?
                    tv_project_name_n.text = projects["name"] as CharSequence?
                    tv_member_list.text = memberList
                    tv_project_details_description_name.text = projects["description"] as CharSequence?
                    tv_status_name.text = projects["status"] as CharSequence?
                }
                viewModel.getTasks(projectname).observe(this) { tasks ->
                    var tasklist = mutableListOf<HashMap<String, String>>()
                    for (i in 0 until tasks.size) {
                        if (tasks[i]["member"]?.contains(username) == true && tasks[i]["status"] == "assigned") {
                            tasklist.add(tasks[i])
//                            var task1 = tasks[i]["name"]+" "+";"+" "+tasks[i]["ddl"]+" "+";"+" "+tasks[i]["status"]
//                            outstanding_tasks_list.text = task1
                        }
                    }
                    if (!tasklist.isEmpty()) {
                        val simpleAdapter = SimpleAdapter(
                            this@ProjectDetailsActivity,
                            tasklist,
                            R.layout.project_details_itemlist,
                            arrayOf("task", "ddl", "status"),
                            intArrayOf(
                                R.id.outstanding_tasks_list_name,
                                R.id.task_deadline,
                                R.id.task_status
                            )
                        )
                        task_list_show.adapter = simpleAdapter
                        task_list_show.onItemClickListener =
                            OnItemClickListener { _: AdapterView<*>?, _: View?, i: Int, _: Long ->
                                println(tasks[i])
//                                tasks[i]["task"]?.let { it1 ->
//                                       viewModel.setTaskToComplete(projectname, it1)
//                                        }
                                if (tasklist[i]["status"] == "assigned") {
                                    showTwoButtonDialog(
                                        "",
                                        "Have you finished " + tasklist[i]["task"] + " ?",
                                        "Yes",
                                        "No",
                                        View.OnClickListener {
                                            tasklist[i]["task"]?.let { it1 ->
                                                viewModel.setTaskToComplete(projectname, it1)
                                                val intent = Intent(
                                                    this@ProjectDetailsActivity,
                                                    ProjectDetailsActivity::class.java
                                                )
                                                var bundle = Bundle()
                                                bundle.putString("User", username)
                                                bundle.putString("Project", projectname)
                                                intent.putExtras(bundle)
                                                finish()
                                                startActivity(intent)
                                            }
                                        },
                                        View.OnClickListener {
                                            val intent = Intent(
                                                this@ProjectDetailsActivity,
                                                ProjectDetailsActivity::class.java
                                            )
                                            var bundle = Bundle()
                                            bundle.putString("User", username)
                                            bundle.putString("Project", projectname)
                                            intent.putExtras(bundle)
                                            startActivity(intent)
                                        })
                                } else {
                                    showTwoButtonDialog(
                                        "",
                                        tasklist[i]["task"] + " is already finished.",
                                        "Yes",
                                        "",
                                        View.OnClickListener {
                                            val intent = Intent(
                                                this@ProjectDetailsActivity,
                                                ProjectDetailsActivity::class.java
                                            )
                                            var bundle = Bundle()
                                            bundle.putString("User", username)
                                            bundle.putString("Project", projectname)
                                            intent.putExtras(bundle)
                                            startActivity(intent)
                                        }, View.OnClickListener {
                                            val intent = Intent(
                                                this@ProjectDetailsActivity,
                                                ProjectDetailsActivity::class.java
                                            )
                                            var bundle = Bundle()
                                            bundle.putString("User", username)
                                            bundle.putString("Project", projectname)
                                            intent.putExtras(bundle)
                                            startActivity(intent)
                                        }
                                    )
                                }
                            }
                    }
                }
            }
        }
        tv_home.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@ProjectDetailsActivity, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
        tv_back.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@ProjectDetailsActivity, MainActivity::class.java)
            var bundle = Bundle()
            bundle.putString("User", username)
            intent.putExtras(bundle)
            finish()
            startActivity(intent)
        }

    }

    private fun showTwoButtonDialog(title: String, alertText: String, confirmText: String, cancelText: String, conFirmListener: View.OnClickListener, cancelListener: View.OnClickListener) {
        mDialog = builderForCustom!!.setTitle(title)
            .setMessage(alertText)
            .setPositiveButton(confirmText, conFirmListener)
            .setNegativeButton(cancelText, cancelListener)
            .createTwoButtonDialog()
        mDialog!!.show()
    }


    override fun onClick(v: View?) {}
}