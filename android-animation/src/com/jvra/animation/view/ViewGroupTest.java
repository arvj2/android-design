package com.jvra.animation.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import com.jvra.animation.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 7/31/2014.
 */
public class ViewGroupTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group_layout);

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.planets)));

    }


    public void animate(View v) {
//        Button target =(Button) findViewById( android.R.id.button1 );
//
//        ObjectAnimator scale = ObjectAnimator.ofFloat( target,"scaleX",0f);
//        scale.setDuration(1000).start();
//        ObjectAnimator scale2 = ObjectAnimator.ofFloat( target,"scaleY",0f);
//        scale2.setDuration(1000).start();
//
//
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(scale, scale2);
//        set.setTarget(target);
//        set.start();

    }
}
