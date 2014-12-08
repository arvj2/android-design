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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ViewAnimator;
import com.jvra.demos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jansel Valentin R. (jrodr) on 08/29/14.
 */
public class BouncingBalls extends Activity {


    private static final String TAG = BouncingBalls.class.getSimpleName();

    private List<ShapeHolder> balls = new ArrayList<ShapeHolder>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bouncing_balls);

        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        container.addView(new AnimatorView(this));
    }

    private class AnimatorView extends View {

        private static final int RED = 0xffFF8080;
        private static final int BLUE = 0xff8080FF;
        private static final int CYAN = 0xff80ffff;
        private static final int GREEN = 0xff80ff80;

        private List<ShapeHolder> holder = new ArrayList<ShapeHolder>();

        private AnimatorView(Context context) {
            super(context);

            ValueAnimator color = ObjectAnimator.ofInt(this, "backgroundColor", RED, BLUE);
            color.setDuration(3000);
            color.setEvaluator(new ArgbEvaluator());
            color.setRepeatCount(ValueAnimator.INFINITE);
            color.setRepeatMode(ValueAnimator.REVERSE);
            color.start();

        }

        protected void onDraw(Canvas canvas) {
            for (ShapeHolder ball : balls) {
                canvas.save();
                canvas.translate(ball.getX(), ball.getY());
                ball.getShape().draw(canvas);
                canvas.restore();
            }
        }

        private ShapeHolder addBall(float x, float y) {
            OvalShape circle = new OvalShape();
            circle.resize(50f, 50f);

            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder holder = new ShapeHolder(drawable);
            holder.setX(x - 25f);
            holder.setY(y - 25f);

            int red = (int) (Math.random() * 255);
            int green = (int) (Math.random() * 255);
            int blue = (int) (Math.random() * 255);

            int color = 0xff000000 | red << 16 | green << 8 | blue;
            Paint paint = drawable.getPaint();

            int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;

            RadialGradient gradient = new RadialGradient(25f, 30f, 10f, color, darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            holder.setPaint(paint);
            balls.add(holder);

            return holder;
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() != MotionEvent.ACTION_DOWN && MotionEvent.ACTION_MOVE != event.getAction()) {
                return false;
            }

            ShapeHolder ball = addBall(event.getX(),event.getY());
            float start = ball.getY();
            float end = getHeight()-70f;
            float h = getHeight();

            long duration = (int) (500* ((h-event.getY())/h));

            ValueAnimator bounceAnim = ObjectAnimator.ofFloat(ball,"y",start,end);
            bounceAnim.setDuration(duration);
            bounceAnim.setInterpolator(new AccelerateInterpolator());

            ValueAnimator sparse = ObjectAnimator.ofFloat(ball,"x",ball.getX(),ball.getX()-25f);
            sparse.setDuration(duration/4);
            sparse.setRepeatCount(1);
            sparse.setRepeatMode(ValueAnimator.REVERSE);
            sparse.setInterpolator(new DecelerateInterpolator());


            ValueAnimator stretchH = ObjectAnimator.ofFloat(ball,"height",ball.getHeight(),ball.getHeight()-25);
            stretchH.setDuration(duration/4);
            stretchH.setRepeatCount(1);
            stretchH.setRepeatMode(ValueAnimator.REVERSE);
            stretchH.setInterpolator(new DecelerateInterpolator());

            ValueAnimator reinforce = ObjectAnimator.ofFloat(ball,"y",end,end+25);
            reinforce.setDuration(duration/4);
            reinforce.setRepeatCount(1);
            reinforce.setRepeatMode(ValueAnimator.REVERSE);
            reinforce.setInterpolator(new DecelerateInterpolator());

            ValueAnimator stretchW = ObjectAnimator.ofFloat(ball,"width",ball.getWidth(),ball.getWidth()+50);
            stretchW.setDuration(duration/4);
            stretchW.setRepeatCount(1);
            stretchW.setRepeatMode(ValueAnimator.REVERSE);
            stretchW.setInterpolator( new BounceInterpolator() );



            ValueAnimator backBouncing = ObjectAnimator.ofFloat(ball,"y",end,start);
            backBouncing.setDuration(duration);
            backBouncing.setInterpolator(new DecelerateInterpolator() );


            AnimatorSet bounce = new AnimatorSet();
            bounce.play(bounceAnim).before(sparse);
            bounce.play(sparse).with(stretchW);
            bounce.play(sparse).with(reinforce);
            bounce.play(sparse).with(stretchH);
            bounce.play(backBouncing).after(reinforce);

            ValueAnimator fade = ObjectAnimator.ofFloat(ball,"alpha",1f,0f);
            fade.setDuration(250);
            fade.addListener( new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    balls.remove( (ShapeHolder)((ObjectAnimator)animation).getTarget());
                }
            });

            AnimatorSet all = new AnimatorSet();
            all.play(bounce).before(fade);

            all.start();
            return true;
        }
    }
}
