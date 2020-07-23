package com.mzakialkhairi.githubsearch.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mzakialkhairi.githubsearch.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val SPLASH_TIME_OUT:Long = 3000

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        Handler().postDelayed({

            startActivity(Intent(this,MainActivity::class.java))

            finish()
        }, SPLASH_TIME_OUT)
    }

}
