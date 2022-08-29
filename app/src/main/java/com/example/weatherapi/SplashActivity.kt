package com.example.weatherapi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapi.fragments.MainFragment

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread {
            Thread.sleep(2000)
            runOnUiThread {
                supportFragmentManager.beginTransaction().replace(R.id.place_holder,MainFragment.newInstance()).commit()
            }
        }.start()

    }
}