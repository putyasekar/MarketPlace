package com.putya.marketplace.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.putya.marketplace.MainActivity
import com.putya.marketplace.R
import com.putya.marketplace.model.Users
import com.putya.marketplace.utils.Constan
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    var googleSignInClient: GoogleSignInClient? = null
    private var auth: FirebaseAuth? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        btn_login_google.onClick {
            signIn()
        }

        tv_signup_link.onClick {
            startActivity<RegisterActivity>()
        }

        btn_login.onClick {
            if (et_email_login.text.isNotEmpty() &&
                et_password_login.text.isNotEmpty()
            ) {
                authUserSignIn(
                    et_email_login.text.toString(),
                    et_password_login.text.toString()
                )
            }
        }
    }

    //authentication sign in email & password
    private fun authUserSignIn(email: String, pass: String) {
        var status: Boolean? = null
        auth?.signInWithEmailAndPassword(email, pass)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity<MainActivity>()
                    finish()
                } else {
                    toast("Login Failed")
                    Log.e("Error", "Message")
                }
            }
    }


    //request sign in google
    private fun signIn() {
        val gson = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gson)
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, 4)

    }

    //authentication firebase sign in
    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        var uid = String()
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(credential)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth?.currentUser
                checkDatabase(task.result?.user?.uid, account)
                uid = user?.uid.toString()
            } else {
            }
        }
    }

    private fun checkDatabase(uid: String?, account: GoogleSignInAccount?) {

        val database = FirebaseDatabase.getInstance()
        val myref = database.getReference(Constan.tb_user)
        val query = myref.orderByChild("uid").equalTo(auth?.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    startActivity<MainActivity>()
                } else {
                    account?.displayName?.let {
                        account.email?.let { it1 ->
                            insetUser(it, it1, "", uid)
                        }
                    }
                }
            }

        })
    }

    //add data user to realtime database
    private fun insetUser(name: String, email: String, hp: String, idUser: String?): Boolean {
        val user = Users()
        user.email = email
        user.name = name
        user.hp = hp
        user.uid = auth?.uid

        val database = FirebaseDatabase.getInstance()
        val key = database.reference.push().key
        val myref = database.getReference(Constan.tb_user)

        myref.child(key ?: "").setValue(user)
        startActivity<AutentikasiHpActivity>(Constan.key to key)
        return true
    }

    //request result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 4) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {

            }
        }
    }
}