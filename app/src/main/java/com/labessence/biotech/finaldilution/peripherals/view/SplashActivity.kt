package com.labessence.biotech.finaldilution.peripherals.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.labessence.biotech.finaldilution.R

/**
 * Created by dawid.chmielewski on 12/26/2016.
 */
class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val waitHandler = Handler()

        waitHandler.postDelayed({
            val intent = Intent(this, StartupActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}
