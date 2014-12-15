package com.jvra.demos.accessibility;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.jvra.demos.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/10/2014.
 */
public class AccessibilityServiceSampleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accessibility_service_sample);

        final EditText text = (EditText) findViewById(android.R.id.text1);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i2, int i3) {
                text.setContentDescription("This " + sequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
