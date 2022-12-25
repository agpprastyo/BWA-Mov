package com.agprastyo.bwamov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.agprastyo.bwamov.R

class OnboradingTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onborading_two)

        val btnHome = findViewById<View>(R.id.btn_home)

        btnHome.setOnClickListener {
            startActivity(Intent(this@OnboradingTwoActivity, OnboradingThreeActivity::class.java))
        }
    }
}