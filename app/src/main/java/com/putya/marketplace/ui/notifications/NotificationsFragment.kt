package com.putya.marketplace.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.putya.marketplace.R
import com.putya.marketplace.activity.LoginActivity
import com.putya.marketplace.model.Users
import com.putya.marketplace.utils.Constan
import kotlinx.android.synthetic.main.fragment_notifications.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    var auth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Constan.tb_user)
        val query = myRef.orderByChild("UID").equalTo(auth?.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (issue in snapshot.children) {
                    val data = issue?.getValue(Users::class.java)
                    showProfile(data)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showProfile(data: Users?) {
        tv_name_profile.text = data?.name
        tv_email_profile.text = data?.email
        tv_hp_profile.text = data?.hp

        btn_sign_out_profile.onClick {
            auth?.signOut()
            startActivity<LoginActivity>()
        }
    }
}