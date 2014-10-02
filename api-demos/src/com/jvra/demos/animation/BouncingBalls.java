package com.jvra.demos.animation;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
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


    private static final String TAG = BouncingBalls.class.getSimpleName();

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

            final int hsize = event.getHistorySize();
            final int pointers = event.getPointerCount();

            Log.e( TAG, "Preparing to register Touch event" );

            Log.e( TAG, "Historical positions\n" );
            for(  int i=0; hsize>i;++i ){
                Log.e( TAG,"At time "+event.getHistoricalEventTime(i) );
                for( int j=0; pointers>j; ++j ){
                    String line = String.format( "pointer %d: (%f,%f) ",event.getPointerId( j ),event.getHistoricalX(j,i), event.getHistoricalY(j,i) );
                    Log.e( TAG, line );
                }
            }

            Log.e( TAG, "Current positions\n" );
            Log.e( TAG, "At time "+event.getEventTime() );
            for(  int i=0; pointers>i;++i ){
                String line = String.format( " pointer %d: (%f,%f)",event.getPointerId(i),event.getX(),event.getY() );
                Log.e( TAG, line );
            }
            return true;
        }
    }
}


