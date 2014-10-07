package com.jvra.animation;

import android.animation.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

/**
 * Created by Jansel Valentin R. (jrodr) on 08/24/14.
 */
public class LayoutAnimationsHideShow extends Activity{

    private int buttons = 1;
    private ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_animation_hide);

        final CheckBox hideGone = (CheckBox) findViewById( R.id.hideGoneCB );

        container = new LinearLayout(this);
        container.setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        for( int i=0;i<6;++i ){
            Button b = new Button(this);
            b.setText( "Click to Hide "+i );
            container.addView( b );
            b.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility( hideGone.isChecked() ? View.GONE : View.INVISIBLE );
                }
            });
        }

        final LayoutTransition transitioner = new LayoutTransition();
        container.setLayoutTransition( transitioner );

        ViewGroup parent = ( ViewGroup ) findViewById( R.id.parent );
        parent.addView(container);

        Button addButton = ( Button )findViewById( R.id.addNewButton );
        addButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for( int i=0; i<container.getChildCount();++i ){
                    View view = ( View ) container.getChildAt(i);
                    view.setVisibility( View.VISIBLE );
                }
            }
        });

        CheckBox customAnimCB = ( CheckBox )findViewById( R.id.customAnimCB );
        customAnimCB.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                long duration;
                if( isChecked ){
                    transitioner.setStagger( LayoutTransition.CHANGE_APPEARING,30 );
                    transitioner.setStagger( LayoutTransition.CHANGE_DISAPPEARING,30 );
                    setupAnimations(transitioner);
                    duration = 500;
                }else{
                    transitioner.setStagger( LayoutTransition.CHANGE_DISAPPEARING,0 );
                    transitioner.setStagger( LayoutTransition.CHANGE_APPEARING,0);

                    transitioner.setAnimator(LayoutTransition.APPEARING,null);
                    transitioner.setAnimator(LayoutTransition.DISAPPEARING,null);
                    transitioner.setAnimator(LayoutTransition.CHANGE_APPEARING,null);
                    transitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,null);
                    duration = 300;
                }
                transitioner.setDuration(duration);
            }
        });
    }

    private void setupAnimations( LayoutTransition transition ){
        PropertyValuesHolder left = PropertyValuesHolder.ofInt( "left",0,1 );
        PropertyValuesHolder top = PropertyValuesHolder.ofInt( "top",0,1 );
        PropertyValuesHolder right = PropertyValuesHolder.ofInt( "right",0,1 );
        PropertyValuesHolder bottom = PropertyValuesHolder.ofInt( "bottom",0,1 );

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat( "scaleX",1f,0f,1f) ;
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat( "scaleY",1f,0f,1f );

        final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(this, left, top, right, bottom, scaleX, scaleY);
        changeIn.setDuration( transition.getDuration( LayoutTransition.CHANGE_APPEARING ) );
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
        changeIn.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("*", "CHANGE_APPEARING");
                View target = (View) (( ObjectAnimator )animation).getTarget();
                target.setScaleX(1f);
                target.setScaleY(1f);
            }
        });

        Keyframe kf1 = Keyframe.ofFloat(0f,0f);
        Keyframe kf2 = Keyframe.ofFloat(.9999f,360f );
        Keyframe kf3 = Keyframe.ofFloat(1f,0f);


        PropertyValuesHolder rotation = PropertyValuesHolder.ofKeyframe("rotation",kf1,kf2,kf3);

        final ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(this,left,top,right,bottom,rotation);
        changeOut.setDuration( transition.getDuration( LayoutTransition.CHANGE_DISAPPEARING));
        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);
        changeOut.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e( "*", "CHANGE_DISAPPEARING" );
                View target = ( View ) (( ObjectAnimator )animation).getTarget();
                target.setRotation(0f);
            }
        });

        final ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f,0f );
        animIn.setDuration( transition.getDuration( LayoutTransition.APPEARING ));
        transition.setAnimator(LayoutTransition.APPEARING, animIn);
        animIn.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e( "*", "APPEARING" );
                View target = ( View ) (( ObjectAnimator )animation).getTarget();
                target.setRotationY(0f);
            }
        });


        final ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f,90f );
        animOut.setDuration( transition.getDuration( LayoutTransition.DISAPPEARING ));
        transition.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        animOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e( "*", "DISAPPEARING" );
                View target = (View) ((ObjectAnimator) animation).getTarget();
                target.setRotationX(0f);
            }
        });
    }
}
