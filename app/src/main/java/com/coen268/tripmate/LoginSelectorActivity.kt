package com.coen268.tripmate

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login_selector.*

class LoginSelectorActivity : AppCompatActivity() {
    var backButtonCount:Int=0
    private var obj=MethodsTG()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_selector)
        val actionBar = supportActionBar
        actionBar!!.hide()
        mobile_login_btn.setOnClickListener {
            startActivity(Intent(this,MobileLoginActivity::class.java))
        }
        email_login_btn.setOnClickListener {
            startActivity(Intent(this,EmailLoginActivity::class.java))
        }
    }
    override fun onBackPressed(){

        if(backButtonCount>=1) {
            var intent: Intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }else{
            obj.TGToastDefault("Press the back button once again to close the application.",this)
            backButtonCount++
        }
    }
}
