package com.jvra.animation;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 8/6/2014.
 */
public class CustomLayoutActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.custom_layout );
    }
}
