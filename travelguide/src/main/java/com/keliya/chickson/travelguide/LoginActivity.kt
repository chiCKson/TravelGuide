package com.keliya.chickson.travelguide

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var backButtonCount:Int=0
    private val objMethods=MethodsTG()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        EmailLogin.setOnClickListener({
            startActivity(Intent(this,EmailLoginActivity::class.java))
        })
        MobileLogin.setOnClickListener({
            startActivity(Intent(this,MobileLoginActivity::class.java))
        })
    }
    override fun onBackPressed(){

        if(backButtonCount>=1) {
            var intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }else{
            objMethods.TGToastDefault("Press the back button once again to close the application.",this)
            backButtonCount++
        }
    }
}
