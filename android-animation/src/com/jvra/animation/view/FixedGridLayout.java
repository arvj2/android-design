package com.jvra.animation.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 7/31/2014.
 */
public class FixedGridLayout extends ViewGroup {

    private static final String T = FixedGridLayout.class.getSimpleName();

    private int cellWidth;
    private int cellHeight;

    public FixedGridLayout(Context context) {
        super(context);
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cellWidthSpec = MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.AT_MOST);
        int cellHeightSpec = MeasureSpec.makeMeasureSpec(cellHeight, MeasureSpec.AT_MOST);

        int count = getChildCount();
        for (int i = 0; count > i; ++i) {
            final View child = getChildAt(i);
            child.measure(cellWidthSpec, cellHeightSpec);
        }

        int minCount = Math.min(count, 3);
        setMeasuredDimension(resolveSize(cellWidth * minCount, widthMeasureSpec), resolveSize(cellHeight * minCount, heightMeasureSpec));
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int cellWidth = this.cellWidth;
        int cellHeight = this.cellHeight;

        int columns = (right - left) / cellWidth;
        columns = Math.max(1, columns);

        int x = 0, y = 0, i = 0;
        int count = getChildCount();

        for (; count > i; ++i) {
            final View child = getChildAt(i);

            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();

            int l = x + ((cellWidth - w) / 2);
            int t = y + ((cellHeight - h) / 2);

            child.layout(l,t,l+w,t+h);

            if ( 0!=i && 0==(i %(columns-1)) ) {
                y += cellHeight;
                x = 0;
            } else {
               x += cellWidth;
            }
        }
    }
}
