package com.keliya.chickson.travelguide

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_other_service.*
import zh.wang.android.yweathergetter4a_demo.MainActivity

class OtherServiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_service)
        weather_btn.setOnClickListener({
           startActivity(Intent(this,MainActivity::class.java))
        })
        currency_btn.setOnClickListener({
            startActivity(Intent(this,CurrencyConverterActicity::class.java))
        })
    }
}
