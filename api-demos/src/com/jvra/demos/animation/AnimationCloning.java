package com.jvra.demos.animation;

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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import com.jvra.demos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 7/14/2014.
 */
public class AnimationCloning extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_cloning);
        final ViewGroup container = (ViewGroup) findViewById(R.id.container);
        final AnimationView animView = new AnimationView(this);
        container.addView( animView );

        Button start = ( Button ) findViewById( R.id.startButton );
        start.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animView.startAnimation();
            }
        });
    }


    private class AnimationView extends View implements ValueAnimator.AnimatorUpdateListener {

        private List<ShapeHolder> balls = new ArrayList<ShapeHolder>();
        AnimatorSet animation;
        private float density;

        private AnimationView(Context context) {
            super(context);
            density = context.getResources().getDisplayMetrics().density;

            ShapeHolder ball = addBall(50f,25f);
            ball = addBall(150f,25f);
            ball = addBall(250f,25f);
            ball = addBall(350f,25f);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }

        private ShapeHolder addBall(float x,float y ){
            OvalShape circle = new OvalShape();
            circle.resize(50f*density,50f*density);

            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder holder = new ShapeHolder(drawable);
            holder.setX(x -25f );
            holder.setY(y - 25f);

            int red = (int)(100 + Math.random() * 155 );
            int green = (int) (100 + Math.random() *155 );
            int blue = (int)( 100 + Math.random() *155 );

            int color = 0xff000000 | red << 16 | green << 8 | blue;
            Paint paint = drawable.getPaint();
            int darkColor = 0xff000000 | red/4 << 16 | green/4 << 8 | blue/4;

            RadialGradient gradient = new RadialGradient(37.5f,12.5f,40f,color,darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            holder.setPaint(paint);

            balls.add(holder);
            return  holder;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            for( int i=0;i<balls.size();++i ){
                canvas.save();
                ShapeHolder holder = balls.get(i);
                canvas.translate( holder.getX(), holder.getY() );
                holder.getShape().draw( canvas );
                canvas.restore();
            }
        }

        private void createAnimation(){
            if( null == animation ){
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(balls.get(0),"y",0f,getHeight()-balls.get(0).getHeight() ).setDuration(500);
                anim1.addUpdateListener(this);

                ObjectAnimator anim2 = anim1.clone();
                anim1.setTarget(balls.get(1));

                ObjectAnimator anim3 = ObjectAnimator.ofFloat( balls.get(2),"y",0f,getHeight()-balls.get(2).getHeight() ).setDuration(500);
                anim3.setInterpolator( new AccelerateInterpolator());

                ObjectAnimator anim4 = ObjectAnimator.ofFloat( balls.get(2),"y",getHeight()-balls.get(2).getHeight(),0 ).setDuration(500);
                anim3.setInterpolator( new DecelerateInterpolator());


                AnimatorSet s1 = new AnimatorSet();
                s1.playSequentially(anim3,anim4);

                anim3.addUpdateListener(this);
                anim4.addUpdateListener(this);

                AnimatorSet s2 = s1.clone();
                s2.setTarget(balls.get(3));

                animation = new AnimatorSet();
                animation.playTogether(anim1,anim2,s1);
                animation.playSequentially(s1,s2);
            }
        }

        public void startAnimation(){
            createAnimation();
            animation.start();
        }
    }

}
