package com.jvra.demos.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/11/2014.
 */
public class ExampleBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e( "ExampleBroadcastReceiver","intent="+intent );

        String action = intent.getAction();
        if( action.equals(Intent.ACTION_TIME_CHANGED) || action.equals(Intent.ACTION_TIME_CHANGED) ){
            AppWidgetManager awm = AppWidgetManager.getInstance(context);

            List<Integer> ids = new ArrayList<Integer>();
            List<String> texts = new ArrayList<String>();
            ExampleAppWidgetConfigure.loadAllTitlePref(context, ids, texts);

            final int N = ids.size();
            for(int i=0;N>i;++i ){
                ExampleWidgetProvider.updateAppWidget(context,awm,ids.get(i),texts.get(i));
            }
        }
    }
}
