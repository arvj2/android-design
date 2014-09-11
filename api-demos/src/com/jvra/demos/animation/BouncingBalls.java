package com.jvra.demos.animation;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.jvra.demos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jansel Valentin R. (jrodr) on 08/29/14.
 */
public class BouncingBalls extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bouncing_balls );

        ViewGroup container = ( ViewGroup ) findViewById( R.id.container );
        container.addView( new AnimatorView(this) );
    }

    private class AnimatorView extends View implements ValueAnimator.AnimatorUpdateListener{

        private static final int RED = 0xffFF8080;
        private static final int BLUE = 0xff8080FF;
        private static final int CYAN = 0xff80ffff;
        private static final int GREEN = 0xff80ff80;

        private List<ShapeHolder> holder = new ArrayList<ShapeHolder>();
        private AnimatorSet animator;


        private AnimatorView(Context context) {
            super(context);

            ValueAnimator anim = ObjectAnimator.ofFloat( this,"backgroundColor",RED,BLUE );
            anim.setRepeatCount(ValueAnimator.INFINITE);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            anim.setDuration(3000);
            anim.setEvaluator(new ArgbEvaluator() );
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
        }
    }
}


