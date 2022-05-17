package com.example.projectmanager.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.R
import com.example.projectmanager.viewModel.ProjectViewModel
import com.example.projectmanager.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : BaseActivity() {
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
                        finish()
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@UserProfileActivity, ManagerList::class.java)
                        var bundle = Bundle()
                        bundle.putString("User", username)
                        intent.putExtras(bundle)
                        finish()
                        startActivity(intent)
                    }
                }
            }
        }
        btn_save.setOnClickListener {
            updateUserProfile()
            if (username != null) {
                viewModel.getUserByName(username, {}, {}).observe(this) { user1 ->
                    if(user1.identity=="member") {
                        val intent = Intent(this@UserProfileActivity, MainActivity::class.java)
                        var bundle = Bundle()
                        bundle.putString("User", username)
                        intent.putExtras(bundle)
                        finish()
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@UserProfileActivity, ManagerList::class.java)
                        var bundle = Bundle()
                        bundle.putString("User", username)
                        intent.putExtras(bundle)
                        finish()
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when  {
            TextUtils.isEmpty(et_profile_name.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_profile_key.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(et_profile_info.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }
    private fun updateUserProfile() {
        if(validateLoginDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))
            val name = et_profile_name.text.toString().trim { it <= ' ' }
            val key = et_profile_key.text.toString().trim { it<= ' ' }
            val info = et_profile_info.text.toString().trim { it<= ' ' }
            viewModel.updateUserProfile(name, key, info)
        }
    }

}