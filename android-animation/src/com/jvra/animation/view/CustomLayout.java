package com.jvra.animation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.RemoteViews;
import com.jvra.animation.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 8/5/2014.
 */

@RemoteViews.RemoteView
public class CustomLayout extends ViewGroup {

    private int leftWidth;
    private int rightWidth;

    private Rect containerFrame = new Rect();
    private Rect childFrame = new Rect();


    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }


    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(),attrs);
    }


    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int count = getChildCount();

        leftWidth = 0;
        rightWidth = 0;

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; count > i; ++i) {
            final View child = getChildAt(i);
            if (View.GONE != child.getVisibility()) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                final LayoutParams p = (LayoutParams) child.getLayoutParams();
                if (LayoutParams.POSITION_LEFT == p.position) {
                    leftWidth = Math.max(leftWidth, child.getMeasuredWidth() + p.leftMargin + p.rightMargin);
                } else if (LayoutParams.POSITION_RIGHT == p.position) {
                    rightWidth = Math.max(rightWidth, child.getMeasuredWidth() + p.leftMargin + p.rightMargin);
                } else {
                    maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + p.leftMargin + p.rightMargin);
                }

                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + p.topMargin + p.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        maxWidth += leftWidth + rightWidth;

        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());


        setMeasuredDimension
                (
                        resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                        resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT)
                );
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        int leftPos = getPaddingLeft();
        int rightPos = right-left-getPaddingRight();

        final int middleLeft = leftPos + leftWidth;
        final int midleRight = rightPos + rightWidth;

        final int parentTop = getPaddingTop();
        final int parentBottom  = bottom - top - getPaddingBottom();

        for( int i=0;count>i;++i ){
            final View child = getChildAt( i );
            if( View.GONE != child.getVisibility() ){
               final LayoutParams p = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                Log.e( "******","width: "+width+", height: "+height );

                if( LayoutParams.POSITION_LEFT == p.position ){
                    containerFrame.left = leftPos + p.leftMargin;
                    containerFrame.right = rightPos + p.rightMargin;
                    leftPos = containerFrame.right;
                }else if( LayoutParams.POSITION_RIGHT == p.position ){
                    containerFrame.right = rightPos - p.rightMargin;
                    containerFrame.left  = leftPos  - p.leftMargin;
                    rightPos = containerFrame.left;
                }else{
                    containerFrame.left = middleLeft + p.leftMargin;
                    containerFrame.right = midleRight - p.rightMargin;
                }

                containerFrame.top = parentTop + p.topMargin;
                containerFrame.bottom = parentBottom + p.bottomMargin;


                Gravity.apply( p.gravity,width,height,containerFrame,childFrame );
                child.layout(childFrame.left,childFrame.top,childFrame.right,childFrame.bottom);
            }
        }
    }


    private static class LayoutParams extends MarginLayoutParams {

        private static final int POSITION_MIDDLE = 0;
        private static final int POSITION_LEFT = 1;
        private static final int POSITION_RIGHT = 2;

        private int position = POSITION_LEFT;
        private int gravity = Gravity.TOP | Gravity.START;

        private LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CustomLayout);
            position = a.getInt(R.styleable.CustomLayout_layout_position, position);
            gravity = a.getInt(R.styleable.CustomLayout_android_layout_gravity, gravity);
            a.recycle();
        }

        private LayoutParams(int width, int height) {
            super(width, height);
        }

        private LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        private LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }


}
