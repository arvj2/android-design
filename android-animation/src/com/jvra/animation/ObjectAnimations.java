package com.jvra.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 7/30/2014.
 */
public class ObjectAnimations extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.objects_animations);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, HitActivity.class));
        return true;
    }

    public void startAnimation(View v) {
        float dest = 0;
        ImageView image = (ImageView) findViewById(R.id.imageView1);
        switch (v.getId()) {
            case R.id.Button01: {
                dest = 360;
                if (image.getRotation() == 360) {
                    Log.e("*********", "Alpha: " + image.getAlpha());
                    dest = 0;
                }

                ObjectAnimator a = ObjectAnimator.ofFloat(image, "rotation", dest);
                a.setDuration(2000);
                a.start();
                break;
            }
            case R.id.Button02:{
                Paint paint = new Paint();
                TextView text = ( TextView )findViewById( R.id.textView1);
                float width = paint.measureText( text.getText().toString() );

                dest = 0 - width;
                if( text.getX() < 0 )
                    dest = 0;

                ObjectAnimator o = ObjectAnimator.ofFloat( text,"x",dest );
                o.setDuration(2000);
                o.start();
                break;
            }
            case R.id.Button03: {
                dest = 1;
                if (image.getAlpha() > 0)
                    dest = 0;
                ObjectAnimator o = ObjectAnimator.ofFloat(image, "alpha", dest);
                o.setDuration(2000);
                o.start();
                break;
            }
            case R.id.Button04:{
                ObjectAnimator out = ObjectAnimator.ofFloat(image,"alpha",0f);
                out.setDuration( 2000 );

                ObjectAnimator trans = ObjectAnimator.ofFloat(image,"translationX",-500f,0f);
                trans.setDuration(2000);

                ObjectAnimator in = ObjectAnimator.ofFloat(image,"alpha",0f,1f);
                in.setDuration(2000);

                AnimatorSet set = new AnimatorSet();
                set.play(trans).with(in).after(out);
                set.start();
                break;
            }
            default:
                break;
        }
    }
}
