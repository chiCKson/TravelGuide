package com.keliya.chickson.travelguide

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressPie
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_knowledge_hub.*

class KnowledgeHubActivity : AppCompatActivity() {
    var uref = FirebaseDatabase.getInstance().getReference("places")
    var adapter: PlaceListAdapter? = null
    lateinit var dialog: ACProgressPie
    var places: ArrayList<Places> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build()
        dialog.show()
        setContentView(R.layout.activity_knowledge_hub)
        populate()
        Handler().postDelayed({
            // This method will be executed once the timer is over
            adapter = PlaceListAdapter(this, places)

            place_list?.adapter = adapter
            adapter?.notifyDataSetChanged()
            dialog.hide()
        }, 3000)
        place_list.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(this,PlacesActivity::class.java)
            intent.putExtra("name", places[position].name)
            intent.putExtra("link",places[position].link)
            startActivity(intent);

        }
    }

    private fun populate() {
        uref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Result will be holded Here
                for (dsp in dataSnapshot.getChildren()) {
                    var eventObj: Places = Places(
                            "" + dataSnapshot.child(dsp.key.toString()).child("place").getValue(String::class.java), ""+dataSnapshot.child(dsp.key.toString()).child("link").getValue(String::class.java))
                    places.add(eventObj)

                }
                Toast.makeText(this@KnowledgeHubActivity, "all read", Toast.LENGTH_LONG).show()

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Toast.makeText(this@KnowledgeHubActivity, error.toString(), Toast.LENGTH_LONG).show()
            }
        })

    }
}
