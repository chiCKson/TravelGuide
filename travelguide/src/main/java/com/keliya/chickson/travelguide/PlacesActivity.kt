package com.keliya.chickson.travelguide

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressPie
import kotlinx.android.synthetic.main.activity_knowledge_hub.*
import kotlinx.android.synthetic.main.activity_places.*

class PlacesActivity : AppCompatActivity() {
    var pNAme:String?=null
    var link:String?=null
    lateinit var dialog: ACProgressPie
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        dialog = ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build()
        dialog.show()
        Handler().postDelayed({
            dialog.hide()
        }, 4000)
        pNAme = intent.getStringExtra("name")
        link=intent.getStringExtra("link")
        webview_places.loadUrl(link)
    }
}
