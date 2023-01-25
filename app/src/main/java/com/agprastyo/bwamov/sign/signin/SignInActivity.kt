package com.agprastyo.bwamov.sign.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.agprastyo.bwamov.home.HomeActivity
import com.agprastyo.bwamov.R
import com.agprastyo.bwamov.sign.signup.SignUpActivity
import com.agprastyo.bwamov.utils.Constants.Companion.URL
import com.agprastyo.bwamov.utils.Preferences
import com.google.firebase.database.*


class SignInActivity : AppCompatActivity() {

    private lateinit var iUsername: String
    private lateinit var iPassword: String

    private lateinit var mDatabase: DatabaseReference

    lateinit var preferences: Preferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        mDatabase = FirebaseDatabase.getInstance(URL).getReference("User")
        preferences = Preferences(this)

        preferences.setValues("onboarding", "1")
        if (preferences.getValues("status").equals("1")) {
            finishAffinity()

            val goHome = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(goHome)
        }

        val btnHome = findViewById<View>(R.id.btn_home)
        val btnDaftar = findViewById<View>(R.id.btn_daftar)


        btnHome.setOnClickListener {

            val userSignin = findViewById<EditText>(R.id.et_username)
            val passSignin = findViewById<EditText>(R.id.et_password)

            iUsername = userSignin.text.toString()
            iPassword = passSignin.text.toString()

            if (iUsername == "") {
                userSignin.error = "Silahkan Tulis Username Anda"
                userSignin.requestFocus()
            } else if (iPassword == "") {
                passSignin.error = "Silahkan Tulis Password Anda"
                passSignin.requestFocus()
            } else {
                pushLogin(iUsername, iPassword)
            }
        }

        btnDaftar.setOnClickListener {
            val goSignUp = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(goSignUp)
        }
        
    }

    private fun pushLogin(iUsername: String, iPassword: String) {

        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)

                if (user == null) {
                    Toast.makeText(this@SignInActivity,
                        "User tidak ditemukan",
                        Toast.LENGTH_LONG)
                        .show()
                } else {
                    if (user.password.equals(iPassword)) {

                        preferences.setValues("nama", user.nama.toString())
                        preferences.setValues("user", user.username.toString())
                        preferences.setValues("url", user.url.toString())
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("saldo", user.saldo.toString())
                        preferences.setValues("status", "1")

                        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignInActivity,
                            "Password Anda Salah",
                            Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignInActivity, databaseError.message,
                    Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}