package com.jvra.android.compat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/30/14.
 */
public class ActionBarHelperHoneycomb extends ActionBarHelper{

    private Menu menuOptions;
    private View refreshIndeterminateProgressView;



    public ActionBarHelperHoneycomb(Activity activity) {
        super(activity);
    }


    @Override
    protected boolean onCreateOptionsMenu(Menu menu) {
        menuOptions = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onRefreshActionItemState(boolean refreshing) {
        if( null == menuOptions )
            return;

        final MenuItem menuItem = menuOptions.findItem( R.id.menu_refresh );
        if( null == menuItem )
            return;

        if( refreshing ){
            if( null == refreshIndeterminateProgressView ) {
                LayoutInflater inflater = (LayoutInflater) getActionBarThemeContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                refreshIndeterminateProgressView = inflater.inflate(R.layout.actionbar_inderminate_progress, null);
            }
            menuItem.setActionView(refreshIndeterminateProgressView);
        }else{
            menuItem.setActionView(null);
        }
    }

    protected Context getActionBarThemeContext(){
        return getActivity();
    }
}
