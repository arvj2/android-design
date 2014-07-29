package com.jvra.tests.actionbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import com.jvra.android.compat.ActionBarActivity;

public class MainActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("******","menu: "+menu);
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
