package com.jvra.demos.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/23/2014.
 */
public class DrawDummie extends Activity{
    private long start = SystemClock.elapsedRealtime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private class DrawView extends View{
        public DrawView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.save();
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.rgb(127,245,112));
            paint.setStrokeWidth(2.0f);
            paint.setStrokeCap(Paint.Cap.SQUARE);

            drawLine(canvas,400,200,100,400,0,100,paint);
        }

        private void drawLine(Canvas c,int x1,int y1,int z1, int x2,int y2,int z2, Paint paint){
            long now = SystemClock.elapsedRealtime();
            final float xrot = (float)((now-start)/1000);
        }
    }
}
