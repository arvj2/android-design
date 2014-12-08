package com.jvra.demos.animation;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.jvra.demos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 11/26/2014.
 */
public class CustomEvaluator extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_custom_evaluator);

        LinearLayout content = (LinearLayout) findViewById(R.id.content);
        final MyAnimationView view = new MyAnimationView(this);
        content.addView(view);

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View raioser) {
                view.startAnimation();
            }
        });
    }


    private class XYHolder {
        private float x;
        private float y;

        private XYHolder(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    private class XYEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            XYHolder start = (XYHolder) startValue;
            XYHolder end = (XYHolder) endValue;
            return new XYHolder(start.getX() + fraction * (end.getX() - start.getX()),
                    end.getY() + fraction * (end.getY() - start.getY()));
        }
    }


    private class XYBall {
        private ShapeHolder holder;

        private XYBall(ShapeHolder holder) {
            this.holder = holder;
        }

        public void setXY(XYHolder xy) {
            holder.setX(xy.getX());
            holder.setY(xy.getY());
        }

        public XYHolder getXY() {
            return new XYHolder(holder.getX(), holder.getY());
        }
    }


    private class MyAnimationView extends View implements ValueAnimator.AnimatorUpdateListener{
        private final List<ShapeHolder> balls = new ArrayList<ShapeHolder>();
        ValueAnimator bounceAnim = null;
        ShapeHolder ball = null;
        XYBall ballHolder = null;


        private MyAnimationView(Context context) {
            super(context);
            ball = createBall(25, 25);
            ballHolder = new XYBall(ball);
        }


        private void createAnimation() {
            if (null == bounceAnim) {
                XYHolder start = new XYHolder(0f, 0f);
                XYHolder end = new XYHolder(300f, 300f);

                bounceAnim = ObjectAnimator.ofObject(ballHolder, "xY", new XYEvaluator(), start, end);
                bounceAnim.setDuration(1500);
                bounceAnim.addUpdateListener(this);
            }
        }

        public void startAnimation() {
            createAnimation();
            bounceAnim.start();
        }

        public ShapeHolder createBall(float x, float y) {
            OvalShape circle = new OvalShape();
            circle.resize(50f,50f);

            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder holder = new ShapeHolder(drawable);
            holder.setX(x-25f);
            holder.setY(y-25f);

            int red = (int)(Math.random() * 255);
            int green = (int)(Math.random() * 255);
            int blue = (int)(Math.random() * 255);


            int color = 0xff000000 | red << 16 | green << 8 | blue;
            int darkColor = 0xff000000 | red/4 << 16 | green/4 << 8 | blue/4;

            Paint paint = drawable.getPaint();
            RadialGradient gradient = new RadialGradient(377.5f,12.5f,50f,color,darkColor,Shader.TileMode.CLAMP );
            paint.setShader(gradient);

            holder.setPaint(paint);
            return holder;
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.save();
            canvas.translate(ball.getX(), ball.getY() );
            ball.getShape().draw(canvas);
            canvas.restore();

        }


        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            invalidate();
        }
    }
}
