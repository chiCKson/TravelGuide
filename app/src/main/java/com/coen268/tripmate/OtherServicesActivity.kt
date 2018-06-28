package com.coen268.tripmate

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_other_services.*
import zh.wang.android.yweathergetter4a_demo.MainActivity


class OtherServicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_services)
        weather_btn.setOnClickListener {
            startActivity(Intent(this,WeatherActivity::class.java))
        }
        currency_btn.setOnClickListener {
            startActivity(Intent(this,CurrencyConverterActicity::class.java))
        }
    }
}
