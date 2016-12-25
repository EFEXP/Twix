package xyz.donot.quetzal.util.rximage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*



class HiddenActivity : RxAppCompatActivity() {

    companion object {
        val IMAGE_SOURCE = "image_source"
        private val TAG = "RxImagePicker"
        private val SELECT_PHOTO = 100
        private val TAKE_PHOTO = 101
    }
    private var cameraPictureUrl: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            handleIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handleIntent(intent)
        } else {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SELECT_PHOTO -> RxImagePicker.with(this).onImagePicked(data?.data)
                TAKE_PHOTO -> RxImagePicker.with(this).onImagePicked(cameraPictureUrl)
            }
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        RxImagePicker.with(this).onDestroy()
    }

    private fun handleIntent(intent: Intent) {
        val sourceType = Sources.values()[intent.getIntExtra(IMAGE_SOURCE, 0)]
        var chooseCode = 0
        var pictureChooseIntent: Intent? = null
        when (sourceType) {
            Sources.CAMERA -> {
                cameraPictureUrl =  FileProvider.getUriForFile(this@HiddenActivity, applicationContext.packageName + ".provider", createImageFile())
                pictureChooseIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl)
                chooseCode = TAKE_PHOTO
            }
            Sources.GALLERY -> {
                if (!checkPermission()) {
                    return
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    pictureChooseIntent = Intent(Intent.ACTION_PICK)
                    pictureChooseIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                } else {
                    pictureChooseIntent = Intent(Intent.ACTION_PICK)
                }
                pictureChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                pictureChooseIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                pictureChooseIntent.type = "image/*"
                chooseCode = SELECT_PHOTO
            }
        }
        startActivityForResult(pictureChooseIntent, chooseCode)
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this@HiddenActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@HiddenActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0)
            return false
        } else {
            return true
        }
    }

    private fun createImageFile(): File? {
        var imageTempFile: File? = null
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(null)
        try {
            imageTempFile = File.createTempFile(
                    timeStamp,
                    ".jpg",
                    storageDir)
        } catch (ex: IOException) {
            Log.e(TAG, ex.toString())
        }

        return imageTempFile
    }




}