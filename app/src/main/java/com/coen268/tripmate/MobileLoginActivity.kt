package com.coen268.tripmate

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressPie
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_mobile_login.*
import java.util.concurrent.TimeUnit

class MobileLoginActivity : AppCompatActivity() {
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var mAuth: FirebaseAuth
    lateinit var dialog: ACProgressPie
    var backButtonCount:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_login)
        val actionBar = supportActionBar
        actionBar!!.hide()
        dialog = ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build()
        mAuth= FirebaseAuth.getInstance()
        verify_btn.setOnClickListener {

            addTPFrefab("+94"+tb_Phone.text.toString())

            verify()
        }
    }
    override fun onBackPressed(){

        if(backButtonCount>=1) {
            var intent: Intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }else{
            Toast.makeText(this,"Press the back button once again to close the application.", Toast.LENGTH_SHORT).show()
            backButtonCount++
        }
    }


    private fun signIn(credential: PhoneAuthCredential ){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    task: Task<AuthResult> ->
                    if(task.isSuccessful){
                        toast("logging successfull")
                        startActivity(Intent(this,Home::class.java))
                    }
                }
    }
    private fun toast(msg:String){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
    }
    private fun verify(){

        dialog.show()
        val phnNo="+94"+tb_Phone.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phnNo,
                60,
                TimeUnit.SECONDS,
                this,
                verificationStateChangedCallbacks
        )
        addTPFrefab(phnNo)
    }
    fun addTPFrefab(num:String){
        val mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        val editor = mPrefs.edit()
        editor.putString("phone_number", num)
        editor.putBoolean("edit_profile_first_use",true)
        editor.commit()
    }

    private val verificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            toast("verified")
            signIn(phoneAuthCredential)
        }

        /* This one is never called: so i assume there's no problem on my part */
        override fun onVerificationFailed(e: FirebaseException) {

            tb_Phone.error = e.toString()
        }

        /* This one is called */
        override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(s, forceResendingToken)
            toast("code sent")
            dialog.hide()

        }

    }
}
