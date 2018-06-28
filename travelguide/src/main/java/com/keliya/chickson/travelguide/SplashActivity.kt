package com.keliya.chickson.travelguide

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login()
    }
    fun login(){

        val mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        if (mPrefs.getBoolean("is_logged_before", false)) {
            startActivity(Intent(this,MainActivity::class.java))
        }else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
