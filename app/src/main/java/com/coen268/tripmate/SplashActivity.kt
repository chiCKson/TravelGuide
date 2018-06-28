package com.coen268.tripmate

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressPie
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var dialog: ACProgressPie
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build()
        if (haveNetworkConnection()) {
            if (!locationEnabled()) {
                val intent = LocationEnabledCheckActivity.newIntent(this)
                startActivity(intent)
            } else {
                mAuth= FirebaseAuth.getInstance()
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) run {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                    dialog.show()
                    login()
                }else{
                    if(mAuth.currentUser==null)
                        startActivity(Intent(this,LoginSelectorActivity::class.java))
                    else {
                        startActivity(Intent(this,Home::class.java))

                    }
                }

            }
        } else {
            val intent = NetworkEnableActivity.newIntent(this)
            startActivity(intent)
        }



    }
    private fun login(){
        Handler().postDelayed({
            dialog.hide()
            if(mAuth.currentUser==null)
                startActivity(Intent(this,LoginSelectorActivity::class.java))
            else {
                startActivity(Intent(this,Home::class.java))

            }
        }, 4000)
    }
    private fun haveNetworkConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo  = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    private fun locationEnabled():Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    }
}
