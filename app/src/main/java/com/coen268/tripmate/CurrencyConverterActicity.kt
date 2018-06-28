package com.coen268.tripmate

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressPie
import com.studioidan.httpagent.HttpAgent
import com.studioidan.httpagent.JsonCallback
import kotlinx.android.synthetic.main.activity_currency_converter_acticity.*
import org.json.JSONObject

class CurrencyConverterActicity : AppCompatActivity() {
    lateinit var dialog: ACProgressPie
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter_acticity)
        dialog = ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build()
        dialog.show()
        HttpAgent.get("http://data.fixer.io/api/latest?access_key=91b68b73b999b4bb373238103e39838f")
                .goJson(object : JsonCallback() {
                    override fun onDone(success: Boolean, jsonObject: JSONObject) {
                        dialog.hide()
                        responseView.text="Base from EURO\n To LKR:"+jsonObject.getJSONObject("rates").getString("LKR")+
                                "\nYou must subscribed to PREMIUM in fixer.io to use converting currency.Until then you can use this json with all rates\n"+
                                jsonObject.toString()
                    }
                })
    }
}
