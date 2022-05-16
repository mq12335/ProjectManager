package com.example.projectmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.R
import com.example.projectmanager.viewModel.ProjectViewModel
import com.example.projectmanager.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        val intent = intent
        val bundle = intent.extras
        val username = bundle?.getString("User")
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        println(username)
        tv_home.setOnClickListener {
            if (username != null) {
                viewModel.getUserByName(username, {}, {}).observe(this) { user1 ->
                    println(user1)
                    if(user1.identity=="member") {
                        val intent = Intent(this@UserProfileActivity, MainActivity::class.java)
                        var bundle = Bundle()
                        bundle.putString("User", username)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@UserProfileActivity, ManagerList::class.java)
                        var bundle = Bundle()
                        bundle.putString("User", username)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                }
            }

        }
    }
}