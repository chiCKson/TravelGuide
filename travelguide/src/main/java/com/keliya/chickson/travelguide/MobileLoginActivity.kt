package com.keliya.chickson.travelguide

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
    private val objMethods=MethodsTG()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_login)
        supportActionBar!!.hide()
        dialog = ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build()
        mAuth= FirebaseAuth.getInstance()
        verify_btn.setOnClickListener{v->
            text_field_boxes.isEnabled=false
            addTPFrefab("+94"+tb_Phone.text.toString())
            //startActivity(Intent(this,ProfileEditActivity::class.java))
            verify()
        }
    }
    private fun signIn(credential: PhoneAuthCredential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    task: Task<AuthResult> ->
                    if(task.isSuccessful){
                       objMethods.TGToastSuccess("Logging Succeeded",this)
                        val mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
                        val editor = mPrefs.edit()
                        editor.putBoolean("is_logged_before", true)
                        editor.commit()
                        startActivity(Intent(this,MainActivity::class.java))
                    }
                }
    }
    private fun verify(){
        text_field_boxes.isEnabled=true
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

        editor.commit()
    }

    private val verificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
           objMethods.TGToastSuccess("Verified",this@MobileLoginActivity)
            signIn(phoneAuthCredential)
        }

        /* This one is never called: so i assume there's no problem on my part */
        override fun onVerificationFailed(e: FirebaseException) {

            text_field_boxes.setError(e.toString(),true)
        }

        /* This one is called */
        override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(s, forceResendingToken)
            objMethods.TGToastInfo("Code sent",this@MobileLoginActivity)
            dialog.hide()

        }

        /* This one is also called */
        override fun onCodeAutoRetrievalTimeOut(s: String?) {
            super.onCodeAutoRetrievalTimeOut(s)

        }
    }
}
