package com.labessence.biotech.finaldilution.peripherals.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by dawid.chmielewski on 12/26/2016.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, StartupActivity.class);
        startActivity(intent);
        finish();
    }
}
