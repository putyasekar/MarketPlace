package com.putya.marketplace.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.putya.marketplace.R
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.startActivity

class RegisterActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener {
            if (tv_email_register.text.isNotEmpty() &&
                tv_name_register.text.isNotEmpty() &&
                tv_telephone_register.text.isNotEmpty() &&
                tv_password_register.text.isNotEmpty()
            ) {
                authUserRegister(
                    tv_email_register.text.toString(),
                    tv_password_register.text.toString()
                )
            }
        }

    }

    //authentication process
    private fun authUserRegister(email: String, password: String): Boolean {
        auth = FirebaseAuth.getInstance()

        var status: Boolean? = null
        val tag = "tag"

        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (insertUser(
                            tv_name_register.text.toString(),
                            tv_email_register.text.toString(),
                            tv_telephone_register.text.toString(),
                            task.result?.user!!
                        )
                    ) {
                        startActivity<LoginActivity>()
                    }
                } else {
                    status = false
                }
            }
        return status!!
    }

    private fun insertUser(
        name: String,
        email: String,
        password: String,
        user: FirebaseUser
    ): Boolean {
        TODO("Not yet implemented")
    }
}