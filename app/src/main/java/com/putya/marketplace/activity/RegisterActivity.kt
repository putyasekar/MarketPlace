package com.putya.marketplace.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.putya.marketplace.MainActivity
import com.putya.marketplace.R
import com.putya.marketplace.model.Users
import com.putya.marketplace.utils.Constan
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        btn_register.onClick {
            signUpUser()
        }


//        btn_register.onClick {
//            if (et_email_register.text.isNotEmpty() &&
//                et_name_register.text.isNotEmpty() &&
//                et_telephone_register.text.isNotEmpty() &&
//                et_password_register.text.isNotEmpty()
//            ) {
//                authUserRegister(
//                    et_email_register.text.toString(),
//                    et_password_register.text.toString()
//                )
//            }
    }

    private fun signUpUser() {
        val username: String = et_name_register.text.toString()
        val email: String = et_email_register.text.toString()
        val password: String = et_password_register.text.toString()
        val hp: String = et_telephone_register.text.toString()

        if (username == "") {
            Toast.makeText(
                this, getString(R.string.text_message_username),
                Toast.LENGTH_LONG
            ).show()
        } else if (email == "") {
            Toast.makeText(
                this, getString(R.string.text_message_email),
                Toast.LENGTH_LONG
            ).show()
        } else if (password == "") {
            Toast.makeText(
                this, getString(R.string.text_message_password),
                Toast.LENGTH_LONG
            ).show()
        } else if (hp == "") {
            Toast.makeText(
                this, getString(R.string.text_message_hp),
                Toast.LENGTH_LONG
            ).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUserID = auth.currentUser!!.uid
                    refUsers =
                        FirebaseDatabase.getInstance().reference.child(getString(R.string.text_user))
                            .child(firebaseUserID)

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserID
                    userHashMap["username"] = username
                    userHashMap["email"] = email
                    userHashMap["hp"] = hp
                    userHashMap["password"] = password

                    refUsers.updateChildren(userHashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Error Message" + task.exception!!.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}

//authentication process
//    private fun authUserRegister(email: String, password: String): Boolean {
//        auth = FirebaseAuth.getInstance()
//
//        var status: Boolean? = null
//        val tag = "tag"
//
//        auth?.createUserWithEmailAndPassword(email, password)
//            ?.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    if (insertUser(
//                            et_name_register.text.toString(),
//                            et_email_register.text.toString(),
//                            et_telephone_register.text.toString(),
//                            task.result?.user!!
//                        )
//                    ) {
//                        startActivity<LoginActivity>()
//                    }
//                } else {
//                    status = false
//                }
//            }
//        return status!!
//    }
//
//    private fun insertUser(
//        name: String,
//        email: String,
//        hp: String,
//        users: FirebaseUser
//
//    ): Boolean {
//        var user = Users()
//        user.uid = users.uid
//        user.name = name
//        user.email = email
//        user.hp = hp
//
//        val database = FirebaseDatabase.getInstance()
//        val key = database.reference.push().key
//        val myRef = database.getReference(Constan.tb_user)
//
//        myRef.child(key!!).setValue(user)
//        return true
//    }
//}