package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
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
        saveButton = findViewById(R.id.profile_imageButton_save)

        updateUI()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        updateUI()
    }

    private fun updateUI() {
        val authInstance: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (authInstance == null) {
            // TODO Don't let the user see this screen without having successfully completed sign-in.
        } else {
            val userBundle = UserBundle(authInstance)

            userBundle.letsGo.downloadUserData().addOnCompleteListener {
                nameText!!.text = userBundle.letsGo.first
                emailText!!.text = userBundle.firebase.email
                cityCountyText!!.text = "${userBundle.letsGo.city}, ${userBundle.letsGo.country}"
            }
        }
    }
}