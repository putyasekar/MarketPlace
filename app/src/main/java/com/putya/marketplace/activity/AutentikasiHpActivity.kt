package com.putya.marketplace.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.putya.marketplace.MainActivity
import com.putya.marketplace.R
import com.putya.marketplace.utils.Constan
import kotlinx.android.synthetic.main.activity_autentikasi_hp.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class AutentikasiHpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autentikasi_hp)

        val key = intent.getStringExtra(Constan.key)
        val database = FirebaseDatabase.getInstance()
        val myref = database.getReference(Constan.tb_user)

        //update realtime database
        tv_submit.onClick {
            if (et_auth_hp.text.toString().isNotEmpty()) {
                myref.child(key!!).child("hp")
                    .setValue(et_auth_hp.text.toString())
                startActivity<MainActivity>()
            }
            else toast("It cannot be empty")
        }
    }
}