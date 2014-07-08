package com.jvra.android.compat;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/30/14.
 */
public abstract class ActionBarHelper {

    private Activity activity;

    protected ActionBarHelper(Activity activity) {
        this.activity = activity;
    }


    public static ActionBarHelper getInstance(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            return new ActionBarHelperICS(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            return new ActionBarHelperHoneycomb(activity);
        return new ActionBarHelperBase(activity);
    }


    protected void onCreate(Bundle savedinstanceState) {
    }

    protected void onPostCreate(Bundle savedInstanceState) {
    }

    protected void onTitleChange(CharSequence title, int color) {
    }

    protected boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    protected Activity getActivity() {
        return activity;
    }

    public MenuInflater getMenuInflater(MenuInflater inflater) {
        return inflater;
    }

    protected abstract void onRefreshActionItemState(boolean refreshing);
}
