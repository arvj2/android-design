package com.jvra.resource;

import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 2/18/2015.
 */
public class GeneralDrawable extends Activity {


    private static int level = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_drawables);
    }


    public void onClick(View v) {
        ImageView b = (ImageView) findViewById(android.R.id.icon1);
        LayerDrawable l = (LayerDrawable) b.getBackground();

        ClipDrawable c2 = (ClipDrawable) l.findDrawableByLayerId(android.R.id.secondaryProgress);
        ClipDrawable c3 = (ClipDrawable) l.findDrawableByLayerId(android.R.id.progress);
        c2.setLevel(c2.getLevel() + (level++));
        Log.e( "****",c2.getLevel()/c2.getIntrinsicWidth() +"");
    }
}
