package com.coen268.tripmate


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressPie
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.coen268.tripmate.models.PlaceResponse
import com.coen268.tripmate.util.Constants.PLACE_ID
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.studioidan.httpagent.HttpAgent
import com.studioidan.httpagent.JsonCallback
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONException
import org.json.JSONObject


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var dialog: ACProgressPie
    private lateinit var mMap: GoogleMap
    private var placeResponseList: MutableList<PlaceResponse>? = null
    var line: Polyline? = null
    var context: Context? = null
     var ltlngList:MutableList<String>?=null
    var PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
    var startPointInitialized:Boolean=false
    var endPointInitialized:Boolean=false
    var mapReady:Boolean=false
    private var obj=MethodsTG()
    // Static LatLng
    var startLatLng = LatLng(6.9271,79.8612)
    lateinit var endLatLng:LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val actionBar = supportActionBar
        actionBar!!.hide()
        placeResponseList = java.util.ArrayList<PlaceResponse>()
        ltlngList=java.util.ArrayList()
        dialog = ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        end_loc.setOnClickListener {
            autoComplete()
            endPointInitialized=true
        }
        near_place.setOnClickListener {
            for (i in 0 until ltlngList!!.size-1){
                fetchPlaces(ltlngList!![i])
            }
        }
        start_loc.setOnClickListener {
            autoComplete()

        }

    }

    fun autoComplete(){
        try {

            val typeFilter = AutocompleteFilter.Builder()
                    .setCountry("LK")
                    .build()
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(this)
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
        } catch (e: GooglePlayServicesRepairableException) {
            // TODO: Handle the error.
        } catch (e: GooglePlayServicesNotAvailableException) {
            // TODO: Handle the error.
            val message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode)
            obj.TGToastWarning(message,this)
        }
    }
    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                if (startPointInitialized) {
                    endLatLng = place.latLng
                    end_loc.text = place.name
                    dialog.show()
                    startPointInitialized=false
                    updateMap()
                }else{
                    startLatLng=place.latLng
                    startPointInitialized=true
                    endPointInitialized=false
                    start_loc.text=place.name
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data)
                obj.TGToastError(""+status+" error",this)

            } else if (resultCode == RESULT_CANCELED) {
                obj.TGToastWarning("Cancelled",this)
            }
        }

    }

    fun makeURL(sourcelat: Double, sourcelog: Double, destlat: Double,
                destlog: Double): String {
        val urlString = StringBuilder()
        urlString.append("http://maps.googleapis.com/maps/api/directions/json")
        urlString.append("?origin=")// from
        urlString.append(java.lang.Double.toString(sourcelat))
        urlString.append(",")
        urlString.append(java.lang.Double.toString(sourcelog))
        urlString.append("&destination=")// to
        urlString.append(java.lang.Double.toString(destlat))
        urlString.append(",")
        urlString.append(java.lang.Double.toString(destlog))
        urlString.append("&sensor=false&mode=driving&alternatives=true")
        return urlString.toString()
    }





    private fun decodePoly(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapReady=true
        mMap.setOnInfoWindowClickListener {
            val intent = Intent(this, PlaceDetails::class.java)
            intent.putExtra(PLACE_ID, it.snippet)
            startActivity(intent)
        }
    }
    private fun updateMap(){
        if (line != null) {
            mMap.clear()
            placeResponseList!!.clear()
            ltlngList!!.clear()
        }
        if (endPointInitialized && mapReady) {
            var urlTopass = makeURL(startLatLng.latitude,
                    startLatLng.longitude, endLatLng.latitude,
                    endLatLng.longitude)
            HttpAgent.get(urlTopass)
                    .goJson(object : JsonCallback() {
                        override fun onDone(success: Boolean, jsonObject: JSONObject) {

                            mMap.addMarker(MarkerOptions().position(endLatLng))
                            mMap.addMarker(MarkerOptions().position(startLatLng))
                            try {
                                // Tranform the string into a json object

                                val routeArray = jsonObject.getJSONArray("routes")
                                val routes = routeArray.getJSONObject(0)
                                val overviewPolylines = routes
                                        .getJSONObject("overview_polyline")
                                val encodedString = overviewPolylines.getString("points")
                                val list = decodePoly(encodedString)
                                for (z in 0 until list.size - 1) {
                                    val src = list[z]
                                    val dest = list[z + 1]
                                    line = mMap.addPolyline(PolylineOptions()
                                            .add(LatLng(src.latitude, src.longitude),
                                                    LatLng(dest.latitude, dest.longitude))
                                            .width(5f).color(Color.BLUE).geodesic(true))
                                }
                                val legs = routes.getJSONArray("legs")
                                val leg = legs.getJSONObject(0)
                                val steps = leg.getJSONArray("steps")
                                for (k in 0 until steps.length() - 1) {
                                    val step = steps.getJSONObject(k)
                                    val start_locs = step.getJSONObject("start_location")
                                    val lat = start_locs.getDouble("lat")
                                    val lng = start_locs.getDouble("lng")
                                    val ltLngStr = StringBuilder()
                                    ltLngStr.append(lat.toString())
                                    ltLngStr.append(",")
                                    ltLngStr.append(lng.toString())
                                    ltlngList!!.add(ltLngStr.toString())


                                }
                                dialog.hide()

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    })
            mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12f))

        }
    }

    private fun fetchPlaces(input: String) {

        val queue = Volley.newRequestQueue(this)
        val o = JSONObject()
        val stringBuilder = StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?query=museums+in+")
        stringBuilder.append(input)
        stringBuilder.append("&key=AIzaSyAIashPBJ0qlaSCq4P0fgGy-vQkOLrtM9s")
        Log.d("REQUEST", stringBuilder.toString())


        val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.POST,
                stringBuilder.toString(),
                o,
                object : Response.Listener<JSONObject> {

                    lateinit var placeResponse: PlaceResponse

                    override fun onResponse(response: JSONObject) {
                        Log.d("EXAMPLE", "Register Response: " + response.toString())
                        val jsonArray = response.optJSONArray("results")
                        for (i in 0 until jsonArray.length()) {
                            try {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val geometry = jsonObject.getJSONObject("geometry")
                                val location = geometry.getJSONObject("location")
                                placeResponse = PlaceResponse()
                                placeResponse.id = jsonObject.getString("place_id")
                                placeResponse.name = jsonObject.getString("name")
                                placeResponse.latitude = location.getString("lat")
                                placeResponse.longitude = location.getString("lng")
                                placeResponseList!!.add(placeResponse)

                                val name = jsonObject.getString("name")
                                val lat = location.getString("lat")
                                val lng = location.getString("lng")
                                mMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(150f)).position(LatLng(lat.toDouble(),lng.toDouble())).title(name).snippet(placeResponse.id))
                                Log.d("Output", name + lat + lng)

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                        }

                    }
                },
                Response.ErrorListener { error -> error.printStackTrace() }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

        queue.add(jsonObjectRequest)


    }
}
