package com.labwhisper.biotech.finaldilution.init.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.solution.view.StartupActivity

/**
 * Created by dawid.chmielewski on 12/26/2016.
 */
class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val intent = Intent(this, StartupActivity::class.java)
        startActivity(intent)
        finish()
    }
}
