package com.example.projectmanager.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.R
import com.example.projectmanager.dialog.CustomDialog
import com.example.projectmanager.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    lateinit var viewModel: UserViewModel
    private var builderForCustom: CustomDialog.Builder? = null
    private var mDialog: CustomDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        lifecycle.addObserver(viewModel)

        builderForCustom = CustomDialog.Builder(this)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        tv_forgot_password.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login -> {
                    logInRegisteredUser()
                }
                R.id.tv_register -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when  {
            TextUtils.isEmpty(et_log_email.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_log_password.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                //showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }
    private fun logInRegisteredUser() {
        if(validateLoginDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))
            val email = et_log_email.text.toString().trim { it <= ' ' }
            val password = et_log_password.text.toString().trim { it<= ' ' }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if(task.isSuccessful){
                        Toast.makeText(
                            this@LoginActivity,
                            "You are logged in successfully!",
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.getUserByEmail(email,{},{}).observe(this) { currentUser ->
                            var bundle = Bundle()
                            bundle.putString("User", currentUser.name)
                            showTwoButtonDialog("", "Do you want to edit your profile?","Yes", "No", View.OnClickListener {
                                val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
                                intent.putExtras(bundle)
                                finish()
                                startActivity(intent)
                            },View.OnClickListener {
                                if(currentUser.identity=="manager") {
                                    val intent = Intent(this@LoginActivity, ManagerList::class.java)
                                    intent.putExtras(bundle)
                                    finish()
                                    startActivity(intent)
                                } else {
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.putExtras(bundle)
                                    finish()
                                    startActivity(intent)
                                }
                            })
                        }
                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    private fun showSingleButtonDialog(title: String, alertText: String, btnText: String, onClickListener:View.OnClickListener) {
        mDialog = builderForCustom!!.setTitle(title)
            .setMessage(alertText)
            .setSingleButton(btnText, onClickListener)
            .createSingleButtonDialog()
        mDialog!!.show()
    }

    private fun showTwoButtonDialog(title: String, alertText: String, confirmText: String, cancelText: String, conFirmListener: View.OnClickListener, cancelListener: View.OnClickListener) {
        mDialog = builderForCustom!!.setTitle(title)
            .setMessage(alertText)
            .setPositiveButton(confirmText, conFirmListener)
            .setNegativeButton(cancelText, cancelListener)
            .createTwoButtonDialog()
        mDialog!!.show()
    }
}