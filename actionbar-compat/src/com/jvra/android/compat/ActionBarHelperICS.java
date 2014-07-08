package com.jvra.android.compat;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/30/14.
 */
public class ActionBarHelperICS extends ActionBarHelperHoneycomb{

    public ActionBarHelperICS(Activity activity) {
        super(activity);
    }

    @Override
    protected Context getActionBarThemeContext() {
        return getActivity().getActionBar().getThemedContext();
    }
}
