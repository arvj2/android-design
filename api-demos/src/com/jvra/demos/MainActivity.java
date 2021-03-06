package com.jvra.demos;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.jvra.demos.accessibility.AccessibilityMainActivity;
import com.jvra.demos.animation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<Entry> adapter = new ArrayAdapter<Entry>(this,android.R.layout.simple_list_item_1,getEntries());
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Entry entry = getEntries().get(position);
        startActivity( new Intent(this,entry.activity ) );
    }

    private List<Entry> getEntries(){
        return Arrays.asList(
                new Entry(AccessibilityMainActivity.class,"Accessibility" ),
                new Entry(AnimationMainActivity.class,"Animations" ),
                new Entry(com.jvra.demos.ContentMainActivity.class,"Content" ),
                new Entry(com.jvra.demos.MediaMainActivity.class,"Media" )
        );
    }

    private class Entry {
        Class<?> activity;
        String title;
        Entry(Class<?> activity, String title ){
            this.activity = activity;
            this.title = title;
        }
        @Override
        public String toString() {
            return title;
        }
    }

}
