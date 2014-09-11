package com.jvra.demos.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jvra.demos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jansel Valentin R. (jrodr) on 08/29/14.
 */
public class AnimatorEvents extends Activity{

    private TextView startText, repeatText, cancelText, endText;
    private TextView startTextAniamtor,repeatTextAnimator, cancelTextAnimator,endTextAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animator_events );

        LinearLayout container = ( LinearLayout ) findViewById( R.id.container );
        final AnimatorView view = new AnimatorView(this);
        container.addView( view );

        startText = ( TextView ) findViewById( R.id.startText );
        startText.setAlpha(.5f);

        repeatText = ( TextView ) findViewById( R.id.repeatText );
        repeatText.setAlpha(.5f);

        cancelText = ( TextView ) findViewById( R.id.cancelText );
        cancelText.setAlpha(0.5f);

        endText = ( TextView ) findViewById( R.id.endText );
        endText.setAlpha(0.5f);

        startTextAniamtor = (TextView) findViewById( R.id.startTextAnimator );
        startTextAniamtor.setAlpha(0.5f);

        repeatTextAnimator = (TextView) findViewById( R.id.repeatTextAnimator );
        repeatTextAnimator.setAlpha(0.5f);

        cancelTextAnimator= (TextView) findViewById( R.id.cancelTextAnimator );
        cancelTextAnimator.setAlpha(0.5f);

        endTextAnimator = (TextView) findViewById( R.id.endTextAnimator );
        endTextAnimator.setAlpha(0.5f);

        final CheckBox endCB = ( CheckBox ) findViewById( R.id.endCB );

        Button starter = ( Button ) findViewById( R.id.startButton );
        starter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.startAnimation(endCB.isChecked());
            }
        });

        Button canceler = ( Button ) findViewById( R.id.cancelButton );
        canceler.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.cancelAnimation();
            }
        });

        Button ender = ( Button ) findViewById( R.id.endButton );
        ender.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.endAnimation();
            }
        });
    }



    private class AnimatorView extends View implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener{

        private final List<ShapeHolder> balls = new ArrayList<ShapeHolder>();
        AnimatorSet animator;
        ShapeHolder ball ;
        boolean endImmediately;


        private AnimatorView(Context context) {
            super(context);
            ball = createBall(25,25);
        }


        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.save();
            canvas.translate(ball.getX(),ball.getY());
            ball.getShape().draw(canvas);
            canvas.restore();
        }


        @Override
        public void onAnimationStart(Animator animation) {
            if( animation instanceof AnimatorSet )
                startText.setAlpha(1f);
            else{
                    startTextAniamtor.setAlpha(1f);
            }

            if(endImmediately)
                animation.end();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if( animation instanceof AnimatorSet )
                endText.setAlpha(1f);
            else
                endTextAnimator.setAlpha(1f);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if( animation instanceof AnimatorSet )
                cancelText.setAlpha(1f);
            else
                cancelTextAnimator.setAlpha(1f);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            if( animation  instanceof  AnimatorSet )
                repeatText.setAlpha(1f);
            else
                repeatTextAnimator.setAlpha(1f);
        }


        private void createAnimation(){
            if( null == animator ){
               ObjectAnimator yAnim = ObjectAnimator.ofFloat(ball,"y",ball.getY(),getHeight()-50f ).setDuration(1500);
                yAnim.setRepeatCount(0);
                yAnim.setRepeatMode( ValueAnimator.REVERSE );
                yAnim.setInterpolator( new AccelerateInterpolator() );
                yAnim.addUpdateListener(this);
                yAnim.addListener(this);

                ObjectAnimator xAnim = ObjectAnimator.ofFloat(ball,"x",ball.getX(),ball.getX()+300 ).setDuration(1000);
                xAnim.setStartDelay(0);
                xAnim.setRepeatMode(0);
                xAnim.setRepeatMode(ValueAnimator.REVERSE);
                xAnim.setInterpolator(new AccelerateInterpolator(2f));

                ObjectAnimator alpha = ObjectAnimator.ofFloat( ball,"alpha",1f,.5f ).setDuration(1000);

                AnimatorSet seq = new AnimatorSet();
                seq.play(alpha);

                animator = new AnimatorSet();
                animator.playTogether(yAnim,xAnim,seq);
                animator.addListener(this);
            }
        }

        public void endAnimation(){
            createAnimation();
            animator.end();
        }

        public void cancelAnimation(){
            createAnimation();
            animator.cancel();
        }

        public void startAnimation( boolean endImmediately ){
            this.endImmediately = endImmediately;
            startText.setAlpha(.5f);
            repeatText.setAlpha(.5f);
            cancelText.setAlpha(.5f);
            endText.setAlpha(.5f);

            startTextAniamtor.setAlpha(0.5f);
            repeatTextAnimator.setAlpha(0.5f);
            cancelTextAnimator.setAlpha(0.5f);
            endTextAnimator.setAlpha(0.5f);

            createAnimation();
            animator.start();
        }

        private ShapeHolder createBall(float x,float y ){
            OvalShape circle = new OvalShape();
            circle.resize(50f, 50f);
            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(x - 25f);
            shapeHolder.setY(y - 25f);
            int red = (int)(Math.random() * 255);
            int green = (int)(Math.random() * 255);
            int blue = (int)(Math.random() * 255);
            int color = 0xff000000 | red << 16 | green << 8 | blue;
            Paint paint = drawable.getPaint(); //new Paint(Paint.ANTI_ALIAS_FLAG);
            int darkColor = 0xff000000 | red/4 << 16 | green/4 << 8 | blue/4;
            RadialGradient gradient = new RadialGradient(37.5f, 12.5f,
                    50f, color, darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            shapeHolder.setPaint(paint);
            return shapeHolder;
        }
    }
}
