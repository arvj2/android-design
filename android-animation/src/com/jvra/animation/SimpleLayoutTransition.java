package com.jvra.animation;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 7/30/2014.
 */
public class SimpleLayoutTransition  extends Activity{

    ViewGroup content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.simple_layout_transition);

        content = (ViewGroup) findViewById( R.id.content );
    }

    public void onSetup( View v ){

        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(500);
        transition.enableTransitionType( LayoutTransition.CHANGING );
        content.setLayoutTransition(transition);
    }

    public void onAdd( View v ){
        Button button = new Button(this);
        button.setText( "Added buttons" );
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        content.addView( button );
    }
}
