package com.jvra.demos.accessibility;

import android.app.Activity;
import android.os.Bundle;
import com.jvra.demos.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/10/2014.
 */
public class TaskListActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist_main);
    }
}
