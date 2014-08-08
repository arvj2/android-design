package com.jvra.animation;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.jvra.animation.atrs.AttributeSetCrawler;
import com.jvra.animation.view.CustomLayout;
import com.jvra.animation.view.ViewGroupTest;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {
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
                new Entry(SimpleLayoutTransition.class, SimpleLayoutTransition.class.getSimpleName()),
                new Entry(ObjectAnimations.class, ObjectAnimations.class.getSimpleName()),
                new Entry(ViewGroupTest.class, ViewGroupTest.class.getSimpleName()),
                new Entry(AttributeSetCrawler.class, AttributeSetCrawler.class.getSimpleName()),
                new Entry(CustomLayoutActivity.class, CustomLayoutActivity.class.getSimpleName()),
                new Entry(FixedGridLayoutActivity.class, FixedGridLayoutActivity.class.getSimpleName())
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
