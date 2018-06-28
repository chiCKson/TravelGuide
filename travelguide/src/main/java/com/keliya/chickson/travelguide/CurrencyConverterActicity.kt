package com.keliya.chickson.travelguide

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.AsyncTask
import android.widget.Toast

import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import org.json.JSONObject
import com.studioidan.httpagent.JsonCallback
import com.studioidan.httpagent.HttpAgent
import kotlinx.android.synthetic.main.activity_currency_converter_acticity.*


class CurrencyConverterActicity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter_acticity)
        HttpAgent.get("http://data.fixer.io/api/latest?access_key=91b68b73b999b4bb373238103e39838f")
                .goJson(object : JsonCallback() {
                    override fun onDone(success: Boolean, jsonObject: JSONObject) {

                        responseView.text="Base from EURO\n To LKR:"+jsonObject.getJSONObject("rates").getString("LKR")+
                                "\nYou must subscribed to PREMIUM in fixer.io to use converting currency.Until then you can use this json with all rates\n"+
                                jsonObject.toString()
                    }
                })
    }

}

