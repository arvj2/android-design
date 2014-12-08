package com.jvra.demos.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import com.jvra.demos.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 11/26/2014.
 */
public class ListFlipper extends Activity {

    private static final int DURATION = 1500;
    private SeekBar seekBar;


    private static final String[] LIST_STRINGS_EN = new String[]{
            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "Six"
    };
    private static final String[] LIST_STRINGS_FR = new String[]{
            "Un",
            "Deux",
            "Trois",
            "Quatre",
            "Le Five",
            "Six"
    };

    private ListView englishList;
    private ListView frenchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotation_list);

        englishList = (ListView) findViewById(R.id.list_en);
        frenchList = (ListView) findViewById(R.id.list_fr);

        final ArrayAdapter<String> esAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, LIST_STRINGS_EN);
        final ArrayAdapter<String> frAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, LIST_STRINGS_EN);

        englishList.setAdapter(esAdapter);
        frenchList.setAdapter(frAdapter);

        frenchList.setRotation(-90f);

        Button starter = (Button) findViewById(R.id.button);
        starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipit();
            }
        });
    }


    private Interpolator accelerator = new AccelerateInterpolator();
    private Interpolator decelerator = new DecelerateInterpolator();

    private void flipit() {
        final ListView visibleList;
        final ListView invisibleList;

        if( englishList.getVisibility() == View.GONE ){
            visibleList = frenchList;
            invisibleList = englishList;
        }else{
            visibleList = englishList;
            invisibleList = frenchList;
        }

        ObjectAnimator anim = ObjectAnimator.ofFloat(visibleList,"rotationY",0f,90f);
        anim.setDuration(500);
        anim.setInterpolator(accelerator);

        final ObjectAnimator anim2 = ObjectAnimator.ofFloat(invisibleList,"rotationY",-90f,0f);
        anim2.setDuration(500);
        anim2.setInterpolator(decelerator);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                visibleList.setVisibility( View.GONE );
                anim2.start();
                invisibleList.setVisibility(View.VISIBLE);
            }
        });

        anim.start();
    }
}
