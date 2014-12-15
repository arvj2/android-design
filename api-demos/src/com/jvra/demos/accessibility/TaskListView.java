package com.jvra.demos.accessibility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/10/2014.
 */
public class TaskListView extends ListView{
    public TaskListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        AccessibilityEvent record = AccessibilityEvent.obtain();
        super.onInitializeAccessibilityEvent(record);

        int priority = (Integer)child.getTag();
        String priorityStr = "Priority: "+priority;
        record.setContentDescription(priorityStr);

        event.appendRecord(record);
        return true;
    }
}



final class TaskListAdapter extends ArrayAdapter<String>{
    TaskListAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }
}