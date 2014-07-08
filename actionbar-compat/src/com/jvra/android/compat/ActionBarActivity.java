package com.jvra.android.compat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/30/14.
 */
public class ActionBarActivity extends Activity {
    private ActionBarHelper actionBarHelper = ActionBarHelper.getInstance(this);

    public ActionBarHelper getActionBarHelper() {
        return actionBarHelper;
    }


    @Override
    public MenuInflater getMenuInflater() {
        return actionBarHelper.getMenuInflater(super.getMenuInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBarHelper.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarHelper.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean canCreate = false;
        canCreate |= actionBarHelper.onCreateOptionsMenu(menu);
        canCreate |= super.onCreateOptionsMenu( menu );
        return canCreate;
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        actionBarHelper.onTitleChange(title, color);
        super.onTitleChanged(title, color);
    }
}
