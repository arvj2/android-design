package com.jvra.demos.animation;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 9/9/2014.
 */
public class BouncingBalls extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BouncingView container = new BouncingView(this);
        container.setLayoutParams( new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        setContentView(container);
    }
}
