package com.jvra.demos.accessibility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.jvra.demos.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/9/2014.
 */
public class CustomViewAccessibilityActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view_accessibility);
    }

    public static class AccessibleCompoundButtonInheritance extends BaseToggleButton {
        public AccessibleCompoundButtonInheritance(Context context, AttributeSet attrs) {
            super(context, attrs);
            Log.e("AccessibleCompoundButtonInheritance", "AccessibleCompoundButtonInheritance");
        }

        @Override
        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(event);
            Log.e("AccessibleCompoundButtonInheritance", "onInitializeAccessibilityEvent");

            event.setChecked(isChecked());
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            Log.e("AccessibleCompoundButtonInheritance", "onInitializeAccessibilityNodeInfo");
            info.setCheckable(true);
            info.setChecked(isChecked());

            CharSequence text = getText();
            if (!TextUtils.isEmpty(text))
                info.setText(text);
        }


        @Override
        public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
            super.onPopulateAccessibilityEvent(event);
            Log.e("AccessibleCompoundButtonInheritance", "onPopulateAccessibilityEvent");

            CharSequence text = getText();
            if (!TextUtils.isEmpty(text))
                event.getText().add(text);
        }
    }


    private static class BaseToggleButton extends View {
        private boolean checked;
        private CharSequence textOn;
        private CharSequence textOff;

        private Layout layoutOn;
        private Layout layoutOff;

        private TextPaint textPaint;

        private BaseToggleButton(Context context) {
            super(context,null,0);
        }

        private BaseToggleButton(Context context, AttributeSet attrs) {
            this(context,attrs,0);
        }

        private BaseToggleButton(Context context, AttributeSet attrs, int style) {
            super(context, attrs, style);


            textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TypedValue typedValues = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.textSize, typedValues, true);

            final int textSize = (int) typedValues.getDimension(context.getResources().getDisplayMetrics());
            textPaint.setTextSize(textSize);

            context.getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValues, true);
            final int textColor = context.getResources().getColor(typedValues.resourceId);
            textPaint.setColor(textColor);

            textOn = context.getString(R.string.accessibility_custom_on);
            textOff = context.getString(R.string.accessibility_custom_off);
        }

        public boolean isChecked() {
            return checked;
        }

        public CharSequence getText() {
            return checked ? textOn : textOff;
        }

        @Override
        public boolean performClick() {
            final boolean handled = super.performClick();
            if (!handled) {
                checked ^= true;
                invalidate();
            }
            return handled;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (null == layoutOn)
                layoutOn = makeLayout(textOn);

            if (null == layoutOff)
                layoutOff = makeLayout(textOff);

            final int minWidth = Math.max(layoutOn.getWidth(), layoutOff.getWidth()) + getPaddingLeft() + getPaddingRight();
            final int minHeigh = Math.max(layoutOn.getHeight(), layoutOff.getHeight()) + getPaddingTop() + getPaddingBottom();

            setMeasuredDimension(resolveSizeAndState(minWidth, widthMeasureSpec, 0),
                    resolveSizeAndState(minHeigh, heightMeasureSpec, 0));
        }

        private Layout makeLayout(CharSequence text) {
            final int width = (int) Math.ceil(Layout.getDesiredWidth(text, textPaint));
            StaticLayout layout = new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.f, 0, true);
            return layout;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingRight());
            Layout layoutText = checked ? layoutOn : layoutOff;
            layoutText.draw(canvas);
            canvas.restore();
        }
    }
}
