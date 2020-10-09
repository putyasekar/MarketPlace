package com.putya.marketplace.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.putya.marketplace.MainActivity
import com.putya.marketplace.R
import org.jetbrains.anko.startActivity

class SplashScreenActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            if (auth?.currentUser?.displayName != null) {
                startActivity<MainActivity>()
            } else startActivity<LoginActivity>()
        }, 3000)

    }
}