package com.jvra.demos.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.jvra.demos.R;

import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/11/2014.
 */
public class ExampleAppWidgetConfigure extends Activity {

    private static final String TAG = ExampleAppWidgetConfigure.class.getSimpleName();
    private static final String PREFS_NAME = "com.example.android.apis.appwidget.ExampleAppWidgetProvider";
    private static final String PREF_PREFIX_KEY = "prefix_";

    int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText appWidgetPrefix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);
        setContentView(R.layout.appwidget_configure);

        appWidgetPrefix = (EditText) findViewById(R.id.appwidget_prefix);
        findViewById(R.id.save_button).setOnClickListener(mOnClickListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (null != extras) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if( widgetId == AppWidgetManager.INVALID_APPWIDGET_ID ){
            finish();
        }

        appWidgetPrefix.setText( loadTitlePref(this,widgetId));
    }


    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Context context = ExampleAppWidgetConfigure.this;

            String prefix = appWidgetPrefix.getText().toString();
            savePrefix(prefix);

            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ExampleWidgetProvider.updateAppWidget(context,manager,widgetId,prefix);

            Intent resultValue  = new Intent();
            resultValue.putExtra( AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId );
            setResult(RESULT_OK,resultValue);
            finish();
        }
    };


    private void savePrefix(String prefix){
        SharedPreferences.Editor prefs = this.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + widgetId, prefix);
        prefs.commit();
    }


    public static final String loadTitlePref(Context context, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String prefix = prefs.getString(PREF_PREFIX_KEY + widgetId, null);
        if (prefix != null) {
            return prefix;
        } else {
            return context.getString(R.string.appwidget_prefix_default);
        }
    }

    public static final void deleteWidgetPrefix(Context context, int widgetId) {

    }

    public static final void loadAllTitlePref(Context context, List<Integer> ids, List<String> texts) {

    }
}
