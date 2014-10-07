package com.jvra.animation;

import android.animation.*;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.jvra.animation.view.FixedGridLayout;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 8/8/2014.
 */
public class FixedGridLayoutActivity extends Activity {

    private int buttons = 1;
    ViewGroup container;
    Animator defaultAppearingAnim, defaultDisappearAnim;
    Animator defaultChangingAppearingAnim, defaultChangingDisappearinAnim;
    Animator customAppearingAnimator, customDisAppearingAnimation;
    Animator customChangingAppearingAnim, customChangingDisAppearingAnim;
    Animator currentAppearingAnim, currentDisappearingAnim;
    Animator currentChangingAppearingAnim, currentChangingDisappearingAnim;
    LayoutTransition transitioner = new LayoutTransition();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixed_grid_layout);

        container = new FixedGridLayout(this);
        container.setClipChildren(false);
        ((FixedGridLayout)container).setCellHeight(100);
        ((FixedGridLayout)container).setCellWidth(200);

        ViewGroup parent = ( ViewGroup ) findViewById( R.id.parent );
        parent.addView( container );
        parent.setClipChildren( false );



        transitioner.setAnimateParentHierarchy(true);
        createCustomAnimations(transitioner);

        container.setLayoutTransition( transitioner );

        defaultAppearingAnim = currentAppearingAnim  = transitioner.getAnimator( LayoutTransition.APPEARING );
        defaultDisappearAnim = currentDisappearingAnim = transitioner.getAnimator( LayoutTransition.DISAPPEARING );
        defaultChangingAppearingAnim = currentChangingAppearingAnim = transitioner.getAnimator( LayoutTransition.CHANGE_APPEARING );
        defaultChangingDisappearinAnim = currentChangingDisappearingAnim = transitioner.getAnimator( LayoutTransition.CHANGE_DISAPPEARING );


        Button b = ( Button ) findViewById( R.id.addButton );
        b.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button n = new Button( FixedGridLayoutActivity.this );
                n.setText( "Button "+(buttons++) );
                n.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        container.removeView(v);
                    }
                });
                container.addView(n, Math.min(0, container.getChildCount() ));
            }
        });

        CheckBox check = ( CheckBox ) findViewById( R.id.customAnimCB );
        check.setOnCheckedChangeListener( CHANGE_LISTENER );

        check = ( CheckBox ) findViewById( R.id.appearingCB );
        check.setOnCheckedChangeListener( CHANGE_LISTENER );


        check = ( CheckBox ) findViewById( R.id.disappearingCB );
        check.setOnCheckedChangeListener( CHANGE_LISTENER );

        check = ( CheckBox ) findViewById( R.id.changingAppearingCB );
        check.setOnCheckedChangeListener( CHANGE_LISTENER );

        check = ( CheckBox ) findViewById( R.id.changingDisappearingCB );
        check.setOnCheckedChangeListener( CHANGE_LISTENER );

    }


    private final CompoundButton.OnCheckedChangeListener CHANGE_LISTENER = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setupTransition(transitioner);
        }
    };


    private void setupTransition( LayoutTransition transitioner ){
        CheckBox custom = ( CheckBox ) findViewById( R.id.customAnimCB );
        CheckBox appear = ( CheckBox ) findViewById( R.id.appearingCB );
        CheckBox disappear = ( CheckBox ) findViewById( R.id.disappearingCB );
        CheckBox cappear = ( CheckBox ) findViewById( R.id.changingAppearingCB);
        CheckBox cdisappear = ( CheckBox ) findViewById( R.id.changingDisappearingCB );

        transitioner.setAnimator(
                LayoutTransition.APPEARING, appear.isChecked() ? ( custom.isChecked() ? customAppearingAnimator : defaultAppearingAnim  )  : null
        );

        transitioner.setAnimator(
               LayoutTransition.DISAPPEARING, disappear.isChecked() ? ( custom.isChecked() ? customDisAppearingAnimation : defaultAppearingAnim ) : null
        );

        transitioner.setAnimator(
                LayoutTransition.CHANGE_APPEARING, cappear.isChecked() ? ( custom.isChecked() ? customChangingAppearingAnim : defaultChangingAppearingAnim) : null
        );

        transitioner.setAnimator(
                LayoutTransition.CHANGE_DISAPPEARING, cdisappear.isChecked() ? ( custom.isChecked() ? customChangingDisAppearingAnim : defaultChangingDisappearinAnim ) : null
        );
    }


    private void createCustomAnimations( LayoutTransition transitioner ){
        PropertyValuesHolder left = PropertyValuesHolder.ofInt( "left",0,1 );
        PropertyValuesHolder top = PropertyValuesHolder.ofInt( "top",0,1 );
        PropertyValuesHolder right = PropertyValuesHolder.ofInt( "right",0,1 );
        PropertyValuesHolder bottom = PropertyValuesHolder.ofInt( "bottom",0,1 );

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat( "scaleX",1f,0f,1f) ;
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat( "scaleY",1f,0f,1f );


        customChangingAppearingAnim = ObjectAnimator.ofPropertyValuesHolder(this,left,top,right,bottom,scaleX,scaleY);
        customChangingAppearingAnim.setDuration( transitioner.getDuration( LayoutTransition.CHANGE_APPEARING ) );
        customChangingAppearingAnim.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e( "*", "CHANGE_APPEARING" );
                View target = (View) (( ObjectAnimator )animation).getTarget();
                target.setScaleX(1f);
                target.setScaleY(1f);
            }
        });

        Keyframe kf1 = Keyframe.ofFloat(0f,0f);
        Keyframe kf2 = Keyframe.ofFloat(.9999f,360f );
        Keyframe kf3 = Keyframe.ofFloat(1f,0f);


        PropertyValuesHolder rotation = PropertyValuesHolder.ofKeyframe("rotation",kf1,kf2,kf3);

        customChangingDisAppearingAnim = ObjectAnimator.ofPropertyValuesHolder(this,left,top,right,bottom,rotation);
        customChangingDisAppearingAnim.setDuration( transitioner.getDuration( LayoutTransition.CHANGE_DISAPPEARING));
        customChangingDisAppearingAnim.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e( "*", "CHANGE_DISAPPEARING" );
                View target = ( View ) (( ObjectAnimator )animation).getTarget();
                target.setRotation(0f);
            }
        });

        customAppearingAnimator = ObjectAnimator.ofFloat(null, "rotationY", 90f,0f );
        customAppearingAnimator.setDuration( transitioner.getDuration( LayoutTransition.APPEARING ));
        customAppearingAnimator.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e( "*", "APPEARING" );
                View target = ( View ) (( ObjectAnimator )animation).getTarget();
                target.setRotationY(0f);
            }
        });


        customDisAppearingAnimation = ObjectAnimator.ofFloat(null, "rotationX", 0f,90f );
        customDisAppearingAnimation.setDuration( transitioner.getDuration( LayoutTransition.DISAPPEARING ));
        customDisAppearingAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e( "*", "DISAPPEARING" );
                View target = (View) ((ObjectAnimator) animation).getTarget();
                target.setRotationX(0f);
            }
        });

    }
}
