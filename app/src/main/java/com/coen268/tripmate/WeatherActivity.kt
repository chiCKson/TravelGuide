package com.coen268.tripmate

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressPie
import com.studioidan.httpagent.HttpAgent
import com.studioidan.httpagent.JsonCallback
import kotlinx.android.synthetic.main.activity_weather.*
import org.json.JSONObject

class WeatherActivity : AppCompatActivity() {
    lateinit var datetxt:String
    lateinit var main:String
    lateinit var desc:String
    lateinit var dialog: ACProgressPie
    var first:Boolean=false
    var adapter: WeatherAdapter? = null
    var weather: ArrayList<Weather> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        val actionBar = supportActionBar
        actionBar!!.hide()
        dialog = ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build()
        search_city_btn.setOnClickListener {
            weather.clear()
            dialog.show()
            getWeather(city_txt.text.toString())
        }


    }
    private fun getWeather(city:String) {
        var urlTopass = StringBuilder()
        urlTopass.append("http://api.openweathermap.org/data/2.5/forecast?q=")
        urlTopass.append(city)
        urlTopass.append(",lk&APPID=")
        urlTopass.append("184369350e9bf8fce6dc1878a8806382")
        HttpAgent.get(urlTopass.toString())
                .goJson(object : JsonCallback() {
                    override fun onDone(success: Boolean, jsonObject: JSONObject) {
                        try {
                            val listArray = jsonObject.getJSONArray("list")
                            for (d in 0 until listArray.length() - 1) {
                                val day = listArray.getJSONObject(d)
                                datetxt = day.getString("dt_txt")
                                val weathers = day.getJSONArray("weather")
                                val w0 = weathers.getJSONObject(0)
                                main = w0.getString("main")
                                desc = w0.getString("description")
                                val weatherObj=Weather(datetxt,main,desc,"")
                                weather.add(weatherObj)
                                //Toast.makeText(this@WeatherActivity, "$datetxt $main $desc", Toast.LENGTH_LONG).show()
                            }
                            dialog.hide()
                            adapter = WeatherAdapter(this@WeatherActivity, weather)
                            weather_list?.adapter = adapter
                            adapter?.notifyDataSetChanged()

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })

    }

}
