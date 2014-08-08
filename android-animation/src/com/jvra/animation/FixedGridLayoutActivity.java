package com.jvra.animation;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import com.jvra.animation.view.FixedGridLayout;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 8/8/2014.
 */
public class FixedGridLayoutActivity extends Activity {

    private int buttons = 1;
    ViewGroup container;
    Animator defaultAppearingAnim, defaultDisappearAnim;
    Animator defaultChangingAppearingAnim, defaultChangingDisappearinAnim;
    Animator customAppearingAnimator, customDisAppearingAnimation;
    Animator customChangingAppearingAnim, customChangingDisAppearingAnim;
    Animator currentAppearingAnim, currentDisappearingAnim;
    Animator currentChangingAppearingAnim, currentChangingDisappearingAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixed_grid_layout);

        container = new FixedGridLayout(this);
        container.setClipChildren(false);
        ((FixedGridLayout)container).setCellHeight(50);
        ((FixedGridLayout)container).setCellWidth(200);
    }
}
