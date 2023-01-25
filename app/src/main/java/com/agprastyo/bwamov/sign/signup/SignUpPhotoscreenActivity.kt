package com.agprastyo.bwamov.sign.signup

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.agprastyo.bwamov.home.HomeActivity
import com.agprastyo.bwamov.R
import com.agprastyo.bwamov.utils.Constants.Companion.URL
import com.agprastyo.bwamov.utils.Preferences
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.util.UUID


class SignUpPhotoscreenActivity : AppCompatActivity(), View.OnClickListener, PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
    private var statusAdd: Boolean = false
    lateinit var filePath: Uri

    private lateinit var storage: FirebaseStorage
    private lateinit var storageReferensi: StorageReference
    private lateinit var preferences: Preferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance(URL)
        storageReferensi = storage.reference



        val textHello = String().format("Selamat datang\n" + intent.getStringExtra("nama"))
        tv_hello.text = textHello


        iv_add.setOnClickListener(this)
        btn_home.setOnClickListener(this)
        btn_Save.setOnClickListener(this)



    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_add -> {
                if (statusAdd) {
                    statusAdd = false
                    btn_Save.visibility = View.VISIBLE
                    iv_add.setImageResource(R.drawable.ic_btn_upload)
                    iv_profile.setImageResource(R.drawable.user_pic)
                } else {
                    Dexter.withContext(this)
                        .withPermission(android.Manifest.permission.CAMERA)
                        .withListener(this)
                        .check()
                }
            }

            R.id.btn_home -> {
                finishAffinity()

                val goHome = Intent(this@SignUpPhotoscreenActivity, HomeActivity::class.java)
                startActivity(goHome)
            }

            R.id.btn_Save -> {
                if (filePath != null) {
                    val progressDialog = ProgressDialog(this)
                    progressDialog.setTitle("Uploading...")
                    progressDialog.show()

                    val ref = storageReferensi.child("images/" + UUID.randomUUID().toString())

                    ref.putFile(filePath)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show()

                            ref.downloadUrl.addOnSuccessListener {
                                preferences.setValues("Url", it.toString())
                            }

                            finishAffinity()
                            val goHome = Intent(this@SignUpPhotoscreenActivity, HomeActivity::class.java)
                            startActivity(goHome)
                        }
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                        }
                        .addOnProgressListener { taskSnapshot ->
                            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                            progressDialog.setMessage("Uploaded" + progress.toInt() + "%")
                        }

                } else {

                }
            }

        }
    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            takePictureIntent -> takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak dapat menambahkan foto profile", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {

    }

    override fun onBackPressed() {
        Toast.makeText(this, "Tergesah? Klik tombol upload anti aja", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            statusAdd = true

            filePath = data.data!!
            Glide.with(this)
                .load(imageBitmap)
                .centerCrop()
                .into(iv_profile)

            btn_Save.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_delete)
        }
    }
}