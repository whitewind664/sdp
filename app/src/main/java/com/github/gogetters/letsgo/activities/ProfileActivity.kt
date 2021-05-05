package com.github.gogetters.letsgo.activities

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.UserBundle
import com.github.gogetters.letsgo.util.PermissionUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ProfileActivity : ActivityCompat.OnRequestPermissionsResultCallback, FirebaseUIActivity() {
    companion object {
        private const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: Int = 2
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: Int = 3
        private const val IMAGE_CAPTURE_REQUEST_CODE = 4

        private const val DIALOG_CAMERA_IDX = 0
        private const val DIALOG_GALLERY_IDX = 1
        private const val DIALOG_CANCEL_IDX = 2
    }

    private lateinit var uploadImageText: TextView
    private lateinit var profileImage: ImageView
    private lateinit var nameText: TextView
    private lateinit var emailText: TextView
    private lateinit var cityCountyText: TextView
    private lateinit var saveButton: ImageButton

    /**
     * Defines whether we want to upload a picture from the camera (true) or from the gallery (false, default)
     */
    private var choosePictureFromCamera: Boolean = false

    private var readPermissionDenied = false
    private var writePermissionDenied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        uploadImageText = findViewById(R.id.profile_textView_uploadImageHint)
        profileImage = findViewById(R.id.profile_imageView_image)
        nameText = findViewById(R.id.profile_textView_name)
        emailText = findViewById(R.id.profile_textView_email)
        cityCountyText = findViewById(R.id.profile_textView_cityCountry)
        saveButton = findViewById(R.id.profile_imageButton_save)
        profileImage.setOnClickListener {
            selectImage()
        }

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

    private fun selectImage() {
        val dialogTexts = arrayOf<CharSequence>(resources.getString(R.string.profile_takePicture), resources.getString(R.string.profile_chooseFromGallery), resources.getString(R.string.profile_cancel))
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.profile_dialogTitle))
        builder.setItems(dialogTexts, DialogInterface.OnClickListener { dialog, clickedIndex ->
            if (clickedIndex == DIALOG_CANCEL_IDX) {
                dialog.dismiss()
                return@OnClickListener
            }
            PermissionUtils.requestPermission(this,
                    WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            choosePictureFromCamera = clickedIndex == DIALOG_CAMERA_IDX
        })
        builder.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE && requestCode != WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            return
        }
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE &&
                PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            readPermissionDenied = false
            getImageFromSelectedSource()
        } else if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE &&
                PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            writePermissionDenied = false
            getImageFromSelectedSource()
        } else {
            // Permission was denied
            when(requestCode) {
                READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> readPermissionDenied = true
                WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> writePermissionDenied = true
            }
        }
    }

    private fun getImageFromSelectedSource() {
        when(choosePictureFromCamera) {
            true -> dispatchCameraIntent()
            false -> dispatchGalleryIntent()
        }
    }

    private fun dispatchCameraIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Log.w("PROFILE", "Camera could not be accessed to take profile picture")
        }
    }

    private fun dispatchGalleryIntent() {

    }
}