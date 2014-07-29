package com.jvra.animation;

import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.*;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onAnim(View v) {
        slideDownAnimation();
    }

    private void slideDownAnimation(){
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0f,1f);
        animation.setDuration(1000);
        set.addAnimation( animation );

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_PARENT,0f,Animation.RELATIVE_TO_SELF,-1f
                );
        animation.setDuration(1500);
        set.addAnimation( animation );
        set.setFillAfter(true);

        ImageView image = ( ImageView )findViewById( android.R.id.icon );

   //     LayoutAnimationController controller = new LayoutAnimationController(set,0.25f);

        image.startAnimation( set );
    }

    private void manualAnimation(){
        ImageView image = ( ImageView ) findViewById( android.R.id.icon );
    }
}
