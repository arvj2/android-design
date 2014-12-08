package com.jvra.demos.animation;

import android.animation.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.jvra.demos.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 11/26/2014.
 */
public class LayoutAnimationHideShow extends Activity {

    private int buttons = 1;
    ViewGroup container;
    private LayoutTransition transitioner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_animation_hidershow);

        final CheckBox hideGone = (CheckBox) findViewById( R.id.hideGoneCB );
        container = new LinearLayout(this);
        container.setLayoutParams( new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        for( int i=0;i<4; ++i ){
            Button button = new Button(this);
            button.setText( String.valueOf(""+i));
            container.addView( button );
            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setVisibility( hideGone.isChecked() ? View.GONE : View.INVISIBLE );
                }
            });
        }

        resetTransition();

        ViewGroup parent = (ViewGroup) findViewById(R.id.parent);
        parent.addView(container);

        Button button = (Button) findViewById( R.id.addNewButton );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for( int i=0;i<container.getChildCount(); ++i ){
                    View v = container.getChildAt(i);
                    v.setVisibility(View.VISIBLE);
                }
            }
        });



        CheckBox customAnim = ( CheckBox ) findViewById( R.id.customAnimCB );
        customAnim.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked ) {
                long duration;
                if( isChecked ){
                    transitioner.setStagger( LayoutTransition.CHANGE_APPEARING,30 );
                    transitioner.setStagger( LayoutTransition.CHANGE_DISAPPEARING,30 );
                    setupCustomAnimation();
                    duration = 500;
                }else{
                    resetTransition();
                    duration = 300;
                }
                transitioner.setDuration(duration);
            }
        });
    }



    private void setupCustomAnimation(){
        PropertyValuesHolder left = PropertyValuesHolder.ofFloat("left",0,1);
        PropertyValuesHolder top = PropertyValuesHolder.ofFloat("top",0,1);
        PropertyValuesHolder right = PropertyValuesHolder.ofFloat("right",0,1);
        PropertyValuesHolder bottom = PropertyValuesHolder.ofFloat("bottom",0,1);

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",1f,0f,1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",1f,0f,1f);

        final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(this,left,top,right,bottom,scaleX,scaleY);
        changeIn.setDuration( transitioner.getDuration(LayoutTransition.CHANGE_APPEARING));
        transitioner.setAnimator(LayoutTransition.APPEARING,changeIn);
        changeIn.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View view = (View) ((ObjectAnimator)animation).getTarget();
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        });


        Keyframe k0 = Keyframe.ofFloat(0f,0f);
        Keyframe k1 = Keyframe.ofFloat(.9999f,360f);
        Keyframe k2 = Keyframe.ofFloat(1f,0f);

        PropertyValuesHolder rotation = PropertyValuesHolder.ofKeyframe("rotation",k0,k1,k2);
        ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(this,left,top,right,bottom,rotation);
        changeOut.setDuration(transitioner.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
        transitioner.setAnimator(LayoutTransition.DISAPPEARING,changeOut);
        changeOut.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View view = ( View ) ((ObjectAnimator)animation).getTarget();
                view.setRotation(0f);
            }
        });


        // Adding
        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).
                setDuration(transitioner.getDuration(LayoutTransition.APPEARING));
        transitioner.setAnimator(LayoutTransition.APPEARING, animIn);
        animIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationY(0f);
            }
        });

        // Removing
        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).
                setDuration(transitioner.getDuration(LayoutTransition.DISAPPEARING));
        transitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        animOut.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationX(0f);
            }
        });


    }

    private void resetTransition(){
        transitioner = new LayoutTransition();
        container.setLayoutTransition(transitioner);
    }
}
