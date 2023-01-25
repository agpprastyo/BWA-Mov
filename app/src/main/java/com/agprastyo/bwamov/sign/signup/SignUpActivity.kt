package com.agprastyo.bwamov.sign.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.agprastyo.bwamov.R
import com.agprastyo.bwamov.sign.signin.User
import com.agprastyo.bwamov.utils.Constants.Companion.URL
import com.google.firebase.database.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var sUsername: String
    private lateinit var sPassword: String
    private lateinit var sNama: String
    private lateinit var sEmail: String

    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseInstance = FirebaseDatabase.getInstance(URL)
        mDatabase = FirebaseDatabase.getInstance(URL).reference
        mDatabaseReference = mFirebaseInstance.getReference("User")

        val btnLanjutkan = findViewById<View>(R.id.btn_lanjutkan)

        btnLanjutkan.setOnClickListener {

            val userSignUp = findViewById<EditText>(R.id.et_username)
            val passSignUp = findViewById<EditText>(R.id.et_password)
            val namaSignUp = findViewById<EditText>(R.id.et_nama)
            val emailSignUp = findViewById<EditText>(R.id.et_email)



            sUsername = userSignUp.text.toString()
            sPassword = passSignUp.text.toString()
            sNama = namaSignUp.text.toString()
            sEmail = emailSignUp.text.toString()

            when {
                sUsername == "" -> {
                    userSignUp.error = "Silahkan isi username anda!"
                    userSignUp.requestFocus()
                }
                sPassword == "" -> {
                    passSignUp.error = "Silahkan isi password anda!"
                    passSignUp.requestFocus()
                }
                sNama == "" -> {
                    namaSignUp.error = "Silahkan isi nama anda!"
                    namaSignUp.requestFocus()
                }
                sEmail == "" -> {
                    emailSignUp.error = "Silahkan isi e-mail anda!"
                    emailSignUp.requestFocus()
                }
                else -> {
                    saveUsername(sUsername, sPassword, sNama, sEmail)
                }
            }

            userSignUp.setText("")
            passSignUp.setText("")
            namaSignUp.setText("")
            emailSignUp.setText("")

//            if (sUsername == "") {
//                userSignUp.error = "Silahkan isi username anda!"
//                userSignUp.requestFocus()
//            } else if (sPassword == "") {
//                passSignUp.error = "Silahkan isi password anda!"
//                passSignUp.requestFocus()
//            } else if (sNama == "") {
//                namaSignUp.error = "Silahkan isi nama anda!"
//                namaSignUp.requestFocus()
//            } else if (sEmail == "") {
//                emailSignUp.error = "Silahkan isi e-mail anda!"
//                emailSignUp.requestFocus()
//            }

        }

    }

    private fun saveUsername(sUsername: String, sPassword: String, sNama: String, sEmail: String) {
        val user = User()

        user.email = sEmail
        user.username = sUsername
        user.nama = sNama
        user.password = sPassword

        if (sUsername != null) {
            checkingUsername(sUsername, user)

        }

    }

    private fun checkingUsername(sUsername: String, data: User) {
        mDatabaseReference.child(sUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)

                if (user == null) {
                    mDatabaseReference.child(sUsername).setValue(data)

                    val goSignUpScreen = Intent(
                        this@SignUpActivity,
                        SignUpPhotoscreenActivity::class.java
                    ).putExtra("data", data.nama)

                    startActivity(goSignUpScreen)

                } else {
                    Toast.makeText(this@SignUpActivity, "User sudah digunakan", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@SignUpActivity,
                    "" + databaseError.message,
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })
    }

}



