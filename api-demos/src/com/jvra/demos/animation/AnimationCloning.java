package com.jvra.demos.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

        final AnimatedView animatedView = new AnimatedView(this);
        container.addView(animatedView);


        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animatedView.startAnimation();
            }
        });
    }


    private static class AnimatedView extends View {

        final List<ShapeHolder> balls = new ArrayList<ShapeHolder>();
        AnimatorSet animatorSet;
        private float density;


        private AnimatedView(Context context) {
            super(context);

            density = getResources().getDisplayMetrics().density;


            ShapeHolder ball1 = addBall(50f, 45f);
            ShapeHolder ball2 = addBall(75f, 50f);
            ShapeHolder ball3 = addBall(45f, 37f);
            ShapeHolder ball4 = addBall(155f, 175f);
        }

        public void startAnimation() {
            if (null == animatorSet) {
                animatorSet = new AnimatorSet();

                ObjectAnimator animator = ObjectAnimator.ofFloat(balls.get(0), "y", 0f, getHeight() - balls.get(0).getWidth()).setDuration(500);
                ObjectAnimator anim2 = animator.clone();
                anim2.setTarget(balls.get(1));
//                anim2.addUpdateListener(this);
            }
        }

        private ShapeHolder addBall(float x, float y) {

            Shape circle = new OvalShape();
            circle.resize(50f * density, 50f * density);

            ShapeDrawable shape = new ShapeDrawable();
            ShapeHolder holder = new ShapeHolder(shape);

            holder.setWidth(x - 25f);
            holder.setHeight(y - 25f);

            Paint paint = shape.getPaint();

            int red = (int) (100 + Math.random() * 155);
            int green = (int) (100 + Math.random() * 155);
            int blue = (int) (100 * Math.random() * 155);

            int color = 0xff000000 | red << 16 | green << 8 | blue;
            int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;

            RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50f, new int[]{color, darkColor}, null, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            shape.setShape(circle);
            balls.add(holder);

            return holder;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }
    }
}
