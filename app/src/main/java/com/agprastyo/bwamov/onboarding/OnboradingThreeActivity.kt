package com.agprastyo.bwamov.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.agprastyo.bwamov.R
import com.agprastyo.bwamov.sign.signin.SignInActivity

class OnboradingThreeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onborading_three)

        val btnHome = findViewById<View>(R.id.btn_home)
        btnHome.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this@OnboradingThreeActivity, SignInActivity::class.java))

        }
    }
}