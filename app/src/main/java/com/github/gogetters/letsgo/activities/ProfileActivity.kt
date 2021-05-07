package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.UserBundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileActivity : FirebaseUIActivity() {

    private var uploadImageText: TextView? = null
    private var profileImage: ImageView? = null
    private var nameText: TextView? = null
    private var emailText: TextView? = null
    private var cityCountyText: TextView? = null
    private var saveButton: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        uploadImageText = findViewById(R.id.profile_textView_uploadImageHint)
        profileImage = findViewById(R.id.profile_imageView_image)
        nameText = findViewById(R.id.profile_textView_name)
        emailText = findViewById(R.id.profile_textView_email)
        cityCountyText = findViewById(R.id.profile_textView_cityCountry)

        // Open friend list with button!
        val friendListButton = findViewById<Button>(R.id.profile_show_friend_list_button)
        friendListButton.setOnClickListener {
            val firebaseUser = FirebaseAuth.getInstance().currentUser

            if (firebaseUser != null) {
                val intent = Intent(this, FriendListActivity::class.java)
//                    .apply {
////                    putExtra(FriendListActivity.EXTRA_USER_UID, firebaseUser.uid)
//                }
                startActivity(intent)
            }
        }

        saveButton = findViewById(R.id.profile_imageButton_save)

        updateUI()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        updateUI()
    }

    private fun updateUI() {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (firebaseUser == null) {
            // TODO Don't let the user see this screen without having successfully completed sign-in.
        } else {
            val userBundle = UserBundle(firebaseUser)

            userBundle.letsGo.downloadUserData().addOnCompleteListener {
                nameText!!.text = userBundle.letsGo.first
                emailText!!.text = userBundle.firebase.email
                cityCountyText!!.text = "${userBundle.letsGo.city}, ${userBundle.letsGo.country}"
            }
        }
    }
}