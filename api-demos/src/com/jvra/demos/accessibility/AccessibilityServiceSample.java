package com.jvra.demos.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/10/2014.
 */
public class AccessibilityServiceSample extends AccessibilityService {

    private static final String TAG = AccessibilityServiceSample.class.getSimpleName();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (null != event.getSource()) {
            AccessibilityNodeInfo info = event.getRecord(event.getRecordCount() - 1).getSource();
            Log.e(TAG, "" + event.getSource().getClassName() + " : " + event.getSource().getText()+" : "+info.getClassName());
        }
    }

    @Override
    public void onInterrupt() {
    }
}
