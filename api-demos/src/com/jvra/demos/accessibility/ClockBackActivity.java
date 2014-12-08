package com.jvra.demos.accessibility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/8/2014.
 */
public class ClockBackActivity extends Activity{

    private static final Intent settingIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onSettings(View view){
        startActivity(settingIntent);
    }
}
