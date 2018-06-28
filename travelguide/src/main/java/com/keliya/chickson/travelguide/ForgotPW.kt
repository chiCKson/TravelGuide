package com.keliya.chickson.travelguide

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_pw.*

class ForgotPW : AppCompatActivity() {
    private var mAuth: FirebaseAuth?=null
    private var obj=MethodsTG()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth= FirebaseAuth.getInstance()
        setContentView(R.layout.activity_forgot_pw)
        reset_pw.setOnClickListener({
            sendResetEmail()
        })

    }
    fun sendResetEmail() {
        var emailAddress:String =tb_email_fpw.text.toString().trim()
        mAuth!!.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(this) { task ->
                    //checking if success
                    if (task.isSuccessful) {
                        obj.TGToastSuccess("Reset link send to the email address you provided.",this)
                        startActivity(Intent(this,EmailLoginActivity::class.java))
                    }

                }
    }
}
