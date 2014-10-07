package com.jvra.demos.animation;

import android.animation.*;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.SeekBar;
import com.jvra.demos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jansel Valentin R. (jrodr) on 08/28/14.
 */
public class AnimationSeeking extends Activity {

    private static final int DURATION = 1500;
    private SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_seeking);

        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        final AnimationView anim = new AnimationView(this);

        container.addView( anim );

        Button start = ( Button ) findViewById( R.id.startButton );
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim.startAnimation();
            }
        });

        seekBar = ( SeekBar ) findViewById( R.id.seekBar );
        seekBar.setMax(DURATION);
        seekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( 0 != anim.getHeight() )
                    anim.seek(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    private class AnimationView extends View implements ValueAnimator.AnimatorUpdateListener,Animator.AnimatorListener {

        private static final int RED = 0xffff8080;
        private static final int BLUE = 0XFF8080FF;
        private static final int CYAN = 0XFF80FFFF;
        private static final int GREEN = 0XFF80FF80;
        private static final float BALL_SIZE = 100f;

        private final List<ShapeHolder> balls = new ArrayList<ShapeHolder>();
        private AnimatorSet animator;
        private ValueAnimator bounceAnim;
        ShapeHolder ball;

        private AnimationView(Context context) {
            super(context);
            ball = addBall(200,0);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            canvas.translate(ball.getX(),ball.getY());
            ball.getShape().draw(canvas);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
           invalidate();
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            balls.remove(((ObjectAnimator)animation).getTarget());
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        private ShapeHolder addBall(float x,float y){

            OvalShape circle = new OvalShape();
            circle.resize(BALL_SIZE,BALL_SIZE);

            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder holder = new ShapeHolder(drawable);
            holder.setX(x);
            holder.setY(y);

            int red = (int)( 100 + Math.random() * 155 );
            int green = (int) ( 100 + Math.random() *155 );
            int blue = (int)(100 + Math.random() *155 );
            int color = 0xff000000 | red <<16 | green <<8 | blue;

            Paint paint = drawable.getPaint();
            int darkColor = 0xff000000 | red/4 <<16 | green/4 <<8 | blue/4;

            RadialGradient gradient = new RadialGradient(37.5f,12.5f,50f,color,darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);

            holder.setPaint(paint);
            balls.add(holder);
            return holder;
        }

        private void createAnimation(){
            if( null == bounceAnim ){
                bounceAnim = ObjectAnimator.ofFloat(ball,"y",ball.getY(),getHeight()-BALL_SIZE).setDuration(DURATION);

                Interpolator interpolator = new AccelerateInterpolator(2);
                bounceAnim.setInterpolator( interpolator );

                bounceAnim.addUpdateListener(this);
            }
        }

        public void startAnimation(){
            createAnimation();
            bounceAnim.start();
        }

        public void seek( int progress ){
            createAnimation();
            bounceAnim.setCurrentPlayTime(progress);
        }
    }
}
