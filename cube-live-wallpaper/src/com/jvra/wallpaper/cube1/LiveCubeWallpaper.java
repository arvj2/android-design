package com.jvra.wallpaper.cube1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/16/2014.
 */
public class LiveCubeWallpaper extends WallpaperService {

    private final Handler mHandler = new Handler();

    @Override
    public Engine onCreateEngine() {
        return new CubeEngine();
    }

    private class CubeEngine extends Engine {
        private final Paint paint;
        private float offset;
        private float touchX = -1;
        private float touchY = -1;
        private long startTime;
        private float centerX;
        private float centerY;

        private boolean visible;

        private final Runnable drawCube = new Runnable() {
            @Override
            public void run() {
                drawFrame();
            }
        };


        public CubeEngine() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setColor(0xFFFFFFFF);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);

            startTime = SystemClock.elapsedRealtime();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(drawCube);
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            this.visible = visible;
            if (visible)
                drawFrame();
            else
                mHandler.removeCallbacks(drawCube);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            visible = false;
            mHandler.removeCallbacks(drawCube);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            offset = xOffset;
            Log.e( "***********","offset="+offset );
            drawFrame();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            centerX = width / 2.f;
            centerY = height / 2.f;
            drawFrame();
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if (MotionEvent.ACTION_MOVE == event.getAction()) {
                touchX = event.getX();
                touchY = event.getY();
            } else {
                touchX = touchY = -1;
            }
            super.onTouchEvent(event);
        }

        private void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    drawCube(c);
                    drawTouchPoint(c);
                }
            } finally {
                if (c != null) {
                    holder.unlockCanvasAndPost(c);
                }
            }


            mHandler.removeCallbacks(drawCube);
            if (visible)
                mHandler.postDelayed(drawCube, 1000 / 25);
        }

        private void drawCube(Canvas canvas) {
            canvas.save();
            canvas.translate(centerX, centerY);
            canvas.drawColor(0xff000000);
        }

        private void drawLine(Canvas canvas, int x1, int y1, int z1, int x2, int y2, int z2) {
            long now = SystemClock.elapsedRealtime();
            float xrot = ((float)(now-startTime))/1000;
            float yrot = (float)(.5-offset) * 2.0f;
            float zrot = 0;
            
        }

        private void drawTouchPoint(Canvas canvas) {
            if( 0<touchX && 0<touchY )
                canvas.drawCircle(touchX,touchY,80,paint);
        }
    }
}
