package com.jvra.demos.accessibility;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.Arrays;
import java.util.List;

public class AccessibilityMainActivity extends ListActivity implements AdapterView.OnItemClickListener {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<Entry> adapter = new ArrayAdapter<Entry>(this, android.R.layout.simple_list_item_1, getEntries());
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Entry entry = getEntries().get(position);
        startActivity(new Intent(this, entry.activity));
    }

    private List<Entry> getEntries() {
        return Arrays.asList(
                new Entry(ClockBackActivity.class, "ClockBackActivity")
        );
    }

    private class Entry {
        Class<?> activity;
        String title;

        Entry(Class<?> activity, String title) {
            this.activity = activity;
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

}
