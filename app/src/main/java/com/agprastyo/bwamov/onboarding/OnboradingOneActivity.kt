package com.agprastyo.bwamov.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.agprastyo.bwamov.R
import com.agprastyo.bwamov.sign.signin.SignInActivity
import com.agprastyo.bwamov.utils.Preferences

class OnboradingOneActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onborading_one)

        preferences = Preferences(this)

        if (preferences.getValues("onboarding").equals("1")) {
            finishAffinity()

            val intent = Intent(this@OnboradingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        val btnHome = findViewById<View>(R.id.btn_home)
        val btnDaftar = findViewById<View>(R.id.btn_daftar)


        btnHome.setOnClickListener {
            val intent = Intent(this@OnboradingOneActivity, OnboradingTwoActivity::class.java)
            startActivity(intent)
        }

        btnDaftar.setOnClickListener {
            preferences.setValues("onboarding", "1")

            finishAffinity()
            val intent = Intent(this@OnboradingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}