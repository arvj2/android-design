package com.jvra.demos.animation;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.jvra.demos.R;

import java.util.ArrayList;

/**
 * Created by Jansel Valentin R. (jrodr) on 08/27/14.
 */
public class AnimationLoading extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_loading );

        ViewGroup container = (ViewGroup) findViewById( R.id.container );
        final AnimationView animView = new AnimationView(this);

        Button start = ( Button )findViewById(R.id.startButton);
        start.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animView.startAnimation();
            }
        });
        container.addView(animView);
    }

    private class AnimationView extends View implements ValueAnimator.AnimatorUpdateListener{

        private static final  float BALL_SIZE = 100f;

        public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();
        AnimatorSet animator;

        private AnimationView(Context context) {
            super(context);

            addBall(50,50);
            addBall(200,50);
            addBall(350,50);
            addBall(500,50, Color.GREEN);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            for ( ShapeHolder ball : balls ){
                canvas.translate(ball.getX(),ball.getY());
                ball.getShape().draw(canvas);
                canvas.translate(-ball.getX(),-ball.getY());
            }
        }

        private void createAnimation(){
            Context context = AnimationLoading.this;

            if( null == animator ){
                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(context,R.animator.object_animator);
                anim.addUpdateListener(this);
                anim.setTarget(balls.get(0));

                ValueAnimator fader = (ValueAnimator) AnimatorInflater.loadAnimator(context,R.animator.animator);
                fader.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        balls.get(1).setAlpha((Float) animation.getAnimatedValue());
                    }
                });

                AnimatorSet seq = ( AnimatorSet ) AnimatorInflater.loadAnimator(context,R.animator.animator_set);
                seq.setTarget(balls.get(2));

                ObjectAnimator colorizer = ( ObjectAnimator ) AnimatorInflater.loadAnimator( context,R.animator.color_animator );
                colorizer.setTarget( balls.get(3));

                animator = new AnimatorSet();
                animator.playTogether(anim,fader,seq,colorizer);
            }
        }


        public void startAnimation(){
            createAnimation();
            animator.start();
        }


        private ShapeHolder createBall(float x, float y ){
            OvalShape circle = new OvalShape();
            circle.resize(BALL_SIZE,BALL_SIZE);
            ShapeDrawable drawable = new ShapeDrawable(circle);

            ShapeHolder holder = new ShapeHolder(drawable);
            holder.setX(x);
            holder.setY(y);
            return holder;
        }


        private void addBall(float x,float y ){
            ShapeHolder ball = createBall(x,y);

            int red = (int)( 100 + Math.random()*155 );
            int green = (int)(100 + Math.random()*155);
            int blue = (int)( 100 +Math.random()*155 );

            int color = 0xff000000 | red << 16 | green << 8 | blue;
            int darkColor = 0xff000000 | red/4 << 16 | green/4 << 8 | blue/4;

            RadialGradient gradient = new RadialGradient(37.5f,12.5f,50f,color,darkColor, Shader.TileMode.CLAMP);
            ball.getShape().getPaint().setShader(gradient);
            balls.add( ball );
        }


        private void addBall( float x, float y, int color ){
            ShapeHolder ball = createBall(x,y);
            ball.setColor(color);
            balls.add(ball);
        }

    }
}
