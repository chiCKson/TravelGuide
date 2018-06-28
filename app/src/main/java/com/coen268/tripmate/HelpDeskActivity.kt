package com.coen268.tripmate

import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_help_desk.*


class HelpDeskActivity : AppCompatActivity() {
    private var userEmail: String? = null
    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_desk)
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth!!.currentUser!!.phoneNumber === "" || firebaseAuth!!.currentUser!!.phoneNumber == null) {
            userEmail = firebaseAuth!!.currentUser!!.email
        } else {
            userEmail = firebaseAuth!!.currentUser!!.phoneNumber
            change_pw.visibility=View.GONE
        }
        change_pw.setOnClickListener {
            startActivity(Intent(this,ForgotPW::class.java))
        }
        contact_us_btn.setOnClickListener {
            val intent = Intent(ACTION_SENDTO) // it's not ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Contacting")
            intent.putExtra(Intent.EXTRA_TEXT, "Hi Travel Guide,\n")
            intent.data = Uri.parse("mailto:tguidelk@gmail.com") // or just "mailto:" for blank
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // this will make such that when user returns to your app, your app is displayed, instead of the email app.
            startActivity(intent)
        }
    }
}
