package com.jvra.demos.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import com.jvra.demos.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/11/2014.
 */
public class ExampleWidgetProvider extends AppWidgetProvider{
    private static final String TAG = ExampleWidgetProvider.class.getSimpleName();


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e( TAG,"onUpdate" );

        final int N = appWidgetIds.length;
        for( int i=0;N>i;++i ){
            final int appWidgetId = appWidgetIds[ i ];
            String prefix = ExampleAppWidgetConfigure.loadTitlePref(context,appWidgetId);
            updateAppWidget(context,appWidgetManager,appWidgetId,prefix);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.e( TAG,"onDeleted" );

        final int N = appWidgetIds.length;
        for( int i=0;N>i;++i ){
            final int widgetId = appWidgetIds[i];
            ExampleAppWidgetConfigure.deleteWidgetPrefix(context, widgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        Log.e( TAG,"onEnable" );

        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName("com.jvra.demos.widget",".ExampleBroadcastReceiver"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        );
    }

    @Override
    public void onDisabled(Context context) {
        Log.e( TAG,"onDisabled" );

        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName("com.jvra.demos.widget",".ExampleBroadcastReceiver"),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        );
    }

    static void updateAppWidget(Context context, AppWidgetManager manager, int widgetId,String prefix ){
        Log.e( TAG,"updateAppWidget appWidgetId=" + widgetId + " titlePrefix=" + prefix);

        CharSequence text = context.getString(
                R.string.appwidget_text_format,
                ExampleAppWidgetConfigure.loadTitlePref(context,widgetId),
                "0x"+Long.toHexString(SystemClock.elapsedRealtime()));

        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.appwidget_provider);
        views.setTextViewText(R.id.appwidget_prefix,text );

        manager.updateAppWidget(widgetId,views);
    }
}
