package com.jvra.demos.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 9/9/2014.
 */
public class BouncingView extends LinearLayout{
    public BouncingView(Context context) {
        super(context);
    }

    public BouncingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BouncingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
