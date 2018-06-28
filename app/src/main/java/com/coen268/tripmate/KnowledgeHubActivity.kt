package com.coen268.tripmate

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressPie
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_knowledge_hub.*

class KnowledgeHubActivity : AppCompatActivity() {
    private var googleApiClient: GoogleApiClient? = null
    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var rootRef: FirebaseFirestore? = null
    private var placesRef: CollectionReference? = null
    private var userEmail: String? = null
    private var userName: String? = null
    var adapter: PlaceListAdapter? = null
    lateinit var dialog: ACProgressPie
    private val emails = java.util.ArrayList<String>()
    var places: ArrayList<PlacesKH> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build()
        dialog.show()
        setContentView(R.layout.activity_knowledge_hub)
        firebaseAuth = FirebaseAuth.getInstance()
        rootRef = FirebaseFirestore.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                val intent = Intent(this@KnowledgeHubActivity, LoginSelectorActivity::class.java)
                startActivity(intent)
            }
        }

        populate()
        Handler().postDelayed({
            // This method will be executed once the timer is over
            adapter = PlaceListAdapter(this, places)

            place_list?.adapter = adapter
            adapter?.notifyDataSetChanged()
            dialog.hide()
        }, 3000)
        place_list.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(this,PlacesKhActivity::class.java)
            intent.putExtra("name", places[position].name)
            intent.putExtra("link",places[position].link)
            startActivity(intent);

        }
    }

    private fun populate() {
        placesRef=rootRef!!.collection("places");
        placesRef!!.addSnapshotListener(EventListener<QuerySnapshot> { documentSnapshots, e ->

            for (snapshot in documentSnapshots!!) {
                places.add(PlacesKH(snapshot.getString("name")!!,snapshot.getString("link")!!))
            }

        })


    }
}
