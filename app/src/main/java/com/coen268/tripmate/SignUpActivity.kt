package com.coen268.tripmate

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
class SignUpActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null
    private var obj=MethodsTG()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth= FirebaseAuth.getInstance()
        setContentView(R.layout.activity_sign_up)
        sign_up_btn.setOnClickListener {
            registerUser()
        }
    }
    private fun clearAll(){
        tb_email_sign.text.clear()
        tb_pw_sign.text.clear()
        tb_pw_confm_sign.text.clear()
    }
    private fun registerUser() {

        //getting email and password from edit texts
        val email = tb_email_sign.text.toString().trim()
        val password = tb_pw_sign.text.toString().trim()
        if (!validateForm()) {

            return
        }
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    //checking if success
                    if (task.isSuccessful) {
                        //display some message here
                        sendEmailVerification()
                        clearAll()
                        obj.TGToastSuccess("Successfully registered",this)
                        startActivity(Intent(this,EmailLoginActivity::class.java))

                    } else {

                        //display some message here
                        obj.TGToastError("Registration Error",this)

                    }
                }
    }
    private fun sendEmailVerification() {
        val user = mAuth!!.currentUser
        user!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        obj.TGToastInfo("Verification email sent to " + user.email!!,this)

                    } else {
                        obj.TGToastError( "Failed to send verification email.",this)
                        //Log.e(FragmentActivity.TAG, "sendEmailVerification", task.exception)

                    }
                }
        // [END send_email_verification]
    }
    private fun validateForm(): Boolean {
        var valid = true
        val email = tb_email_sign.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            text_field_boxes_email_sign.setError("required.",true)
            valid = false
        } else {
            text_field_boxes_email_sign.removeError()
        }
        val password = tb_pw_sign.text.toString().trim()
        if (TextUtils.isEmpty(password)) {
            text_field_boxes_pw_sign.setError("required.",true)
            valid = false
        } else {
            text_field_boxes_pw_sign.removeError()
        }
        val passwordConfirm = tb_pw_confm_sign.text.toString().trim()
        if (TextUtils.isEmpty(passwordConfirm)) {
            text_field_boxes_pw_confm_sign.setError("required.",true)
            valid = false
        } else {
            text_field_boxes_pw_confm_sign.removeError()
        }
        if(password!=passwordConfirm){
            text_field_boxes_pw_confm_sign.setError("Password not matched.",true)
            valid = false
        } else {
            text_field_boxes_pw_confm_sign.removeError()
        }
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            text_field_boxes_email_sign.removeError()

        }else{
            text_field_boxes_email_sign.setError("Enter a valid email.",true)

        }
        if(password.length>6){
            text_field_boxes_pw_sign.removeError()

        }else{
            text_field_boxes_pw_sign.setError("password should be more than 6 characters.",true)

        }
        return valid
    }
}
