package com.coen268.tripmate

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_email_login.*
class EmailLoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null
    private var obj=MethodsTG()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)
        supportActionBar!!.hide()

        mAuth= FirebaseAuth.getInstance()
        sign_up.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        fgt_pw.setOnClickListener {
            startActivity(Intent(this,ForgotPW::class.java))
        }
        login_btn.setOnClickListener {
            loginUser()
        }
    }
    private fun loginUser() {
        //getting email and password from edit texts
        val email = tb_email.text.toString().trim()
        val password = tb_pass.text.toString().trim()
        if (!validateForm()) {
            return
        }
        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    //checking if success
                    if (task.isSuccessful) {
                        login()
                        clearAll()

                    } else {
                        //display some message here
                        obj.TGToastError("Invalid Credentials.",this)

                    }
                }
    }
    fun login(){
        startActivity(Intent(this,Home::class.java))
    }
    private fun validateForm(): Boolean {
        var valid = true
        val email = tb_email.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            text_field_boxes_email.setError("required.",true)
            valid = false
        } else {
            text_field_boxes_email.removeError()
        }
        val password = tb_pass.text.toString().trim()
        if (TextUtils.isEmpty(password)) {
            text_field_boxes_pw.setError("required.",true)
            valid = false
        } else {
            text_field_boxes_pw.removeError()
        }
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            text_field_boxes_email.removeError()
        }else{
            text_field_boxes_email.setError("Enter a valid email.",true)
        }
        return valid
    }
    private fun clearAll(){
        tb_email.text.clear()
        tb_pass.text.clear()
    }
}
