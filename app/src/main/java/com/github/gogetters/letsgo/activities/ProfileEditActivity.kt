 package com.github.gogetters.letsgo.activities

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.cache.Cache
import com.github.gogetters.letsgo.database.ImageStorageService
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.github.gogetters.letsgo.database.user.UserBundle
import com.github.gogetters.letsgo.database.user.UserBundleProvider
import com.github.gogetters.letsgo.util.PermissionUtils
import java.util.*

class ProfileEditActivity : ActivityCompat.OnRequestPermissionsResultCallback, AppCompatActivity() {
    companion object {
        // Codes used when creating a permission request. Used in the onRequestPermissionResult handler.
        private const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: Int = 2
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: Int = 3

        // Codes used when creating an external activity. Used in the onActivityResult handler.
        private const val CAMERA_ACTIVITY_REQUEST_CODE = 0
        private const val GALLERY_SELECTION_ACTIVITY_REQUEST_CODE = 1

        private const val DIALOG_CAMERA_IDX = 0
        private const val DIALOG_GALLERY_IDX = 1
        private const val DIALOG_CANCEL_IDX = 2
    }

    private val tag = "Profile"

    private lateinit var userBundleProvider: UserBundleProvider
    private lateinit var userBundle: UserBundle
    private lateinit var user: LetsGoUser

    private lateinit var nickEditText: EditText
    private lateinit var firstEditText: EditText
    private lateinit var lastEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var cityEditText: EditText

    private lateinit var saveButton: Button

    private lateinit var profileEditImage: ImageView

    private lateinit var profilePictureUri: Uri

    /**
     * Defines whether we want to upload a picture from the camera (true) or from the gallery (false, default)
     */
    private var choosePictureFromCamera: Boolean = false

    private var readPermissionDenied = false
    private var writePermissionDenied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        userBundleProvider = intent.getSerializableExtra("UserBundleProvider") as UserBundleProvider
        userBundle = userBundleProvider.getUserBundle()!!
        user = userBundle.getUser()

        saveButton = findViewById(R.id.profile_edit_button_save)
        saveButton.setOnClickListener {
            user.nick = nickEditText.text.toString()
            user.first = firstEditText.text.toString()
            user.last = lastEditText.text.toString()
            user.country = countryEditText.text.toString()
            user.city = cityEditText.text.toString()

            saveData()
            user.uploadUserData().continueWith { returnToProfile() }
        }


        profileEditImage = findViewById(R.id.profile_edit_imageView_image)
        profileEditImage.setOnClickListener {
            selectImage()
        }

        nickEditText = findViewById(R.id.profile_edit_nick)
        firstEditText = findViewById(R.id.profile_edit_first)
        lastEditText = findViewById(R.id.profile_edit_last)
        countryEditText = findViewById(R.id.profile_edit_country)
        cityEditText = findViewById(R.id.profile_edit_city)

        nickEditText.setText(user.nick)
        firstEditText.setText(user.first)
        lastEditText.setText(user.last)
        countryEditText.setText(user.country)
        cityEditText.setText(user.city)

        ImageStorageService.getProfileImageFromCloud(
            ImageStorageService.PROFILE_PICTURE_PREFIX_CLOUD,
            user.profileImageRef,
            ImageStorageService.getOutputImageFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES)),
            profileEditImage
        )
    }

    private fun returnToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("UserBundleProvider", userBundleProvider)
        startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(tag, "PROFILE EDIT - onActivityResult $requestCode \t $resultCode")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_ACTIVITY_REQUEST_CODE -> onCameraResult(data)
                GALLERY_SELECTION_ACTIVITY_REQUEST_CODE -> onGalleryResult(data)
            }
        }
    }


    private fun selectImage() {
        val dialogTexts = arrayOf<CharSequence>(
            resources.getString(R.string.profile_takePicture),
            resources.getString(R.string.profile_chooseFromGallery),
            resources.getString(R.string.profile_cancel)
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.profile_dialogTitle))
        builder.setItems(dialogTexts, DialogInterface.OnClickListener { dialog, clickedIndex ->
            if (clickedIndex == DIALOG_CANCEL_IDX) {
                dialog.dismiss()
                return@OnClickListener
            }
            PermissionUtils.requestPermission(
                this,
                WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE,
                WRITE_EXTERNAL_STORAGE
            )
            PermissionUtils.requestPermission(
                this,
                READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE,
                READ_EXTERNAL_STORAGE
            )
            choosePictureFromCamera = clickedIndex == DIALOG_CAMERA_IDX
        })
        builder.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE && requestCode != WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            return
        }
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE &&
            PermissionUtils.isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            readPermissionDenied = false
            getImageFromSelectedSource()
        } else if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE &&
            PermissionUtils.isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            writePermissionDenied = false
            getImageFromSelectedSource()
        } else {
            // Permission was denied
            when (requestCode) {
                READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> readPermissionDenied = true
                WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> writePermissionDenied = true
            }
        }
    }

    private fun getImageFromSelectedSource() {
        when (choosePictureFromCamera) {
            true -> dispatchCameraIntent()
            false -> dispatchGalleryIntent()
        }
    }

    private fun dispatchCameraIntent() {
        profilePictureUri = FileProvider.getUriForFile(
            this,
            applicationContext.packageName + ".provider",
            ImageStorageService.getOutputImageFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        )

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, profilePictureUri)
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        try {
            startActivityForResult(takePictureIntent, CAMERA_ACTIVITY_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Log.w("PROFILE", "Camera could not be accessed to take profile picture")
        }
    }

    private fun dispatchGalleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                resources.getString(R.string.profile_gallerySelectionTitle)
            ),
            GALLERY_SELECTION_ACTIVITY_REQUEST_CODE
        )
    }


    private fun onCameraResult(data: Intent?) {
        profileEditImage.setImageURI(profilePictureUri)
        // store the uri for the user
        ImageStorageService.storeProfileImageOnCloud(
            userBundleProvider.getUserBundle()!!.getUser(), profilePictureUri,
            ImageStorageService.PROFILE_PICTURE_PREFIX_CLOUD
        )
    }

    private fun onGalleryResult(data: Intent?) {
        if (data != null && data.data != null) {
            val selectedPhotoUri = data.data
            try {
                selectedPhotoUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            selectedPhotoUri
                        )
                        profileEditImage.setImageBitmap(bitmap)
                    } else {
                        val source =
                            ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        profileEditImage.setImageBitmap(bitmap)
                    }
                    // store the uri for the user
                    ImageStorageService.storeProfileImageOnCloud(
                        userBundleProvider.getUserBundle()!!.getUser(), it,
                        ImageStorageService.PROFILE_PICTURE_PREFIX_CLOUD
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun saveData() {
        val sP: SharedPreferences = getSharedPreferences(Cache.PREF_ID, Context.MODE_PRIVATE)
        Cache.saveUserProfile(sP, user)
    }
}