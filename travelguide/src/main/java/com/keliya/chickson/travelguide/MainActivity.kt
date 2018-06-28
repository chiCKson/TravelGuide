package com.keliya.chickson.travelguide

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var backButtonCount:Int=0
    private var menuTitles: Array<String>? = null
    private val objMethods=MethodsTG()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        menu_btn.setOnClickListener({

            drawer_layout.openDrawer(left_drawer)
        })
        menuTitles = resources.getStringArray(R.array.menu)

        left_drawer!!.adapter = ArrayAdapter(this,
                R.layout.drawer_list_item, menuTitles)
        left_drawer.onItemClickListener = DrawerItemClickListener()
    }
    private inner class DrawerItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            selectItem(position)
        }
    }
    private fun selectItem(position: Int) {
        //Toast.makeText(this,position.toString(),Toast.LENGTH_LONG).show()
        if (position==6){
            FirebaseAuth.getInstance().signOut()
            val mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
            val editor = mPrefs.edit()
            editor.putBoolean("is_logged_before", false)
            editor.commit()
            startActivity(Intent(this,LoginActivity::class.java))
        }

        if(position==4){
            startActivity(Intent(this,KnowledgeHubActivity::class.java))
        }
        if (position==5){
            startActivity(Intent(this,OtherServiceActivity::class.java))
        }
        left_drawer!!.setItemChecked(position, true)
        drawer_layout!!.closeDrawer(left_drawer)
    }
    override fun onBackPressed(){

        if(backButtonCount>=1) {
            var intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }else{
            objMethods.TGToastDefault("Press the back button once again to close the application.",this)
            backButtonCount++
        }
    }
}
