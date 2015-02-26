package com.jvra.loader;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void loadImage(View v) {
        long maxMemory = Runtime.getRuntime().maxMemory()/1024;
        Log.e( "Max Memory",""+maxMemory );

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Log.e( "******","density"+metrics.density+",heightPixels"+metrics.heightPixels+",xdpi"+metrics.xdpi+",densityDpi"+metrics.densityDpi+",scaledDensity"+metrics.scaledDensity+",widthPixels"+metrics.widthPixels+",ydpi"+metrics.ydpi );

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        ImageView image = new ImageView(this);
//        image.setAdjustViewBounds(true);
//
//        final int dim = (int) (200 * metrics.density);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dim, dim);
//        params.gravity = Gravity.CENTER_HORIZONTAL;
//
//        image.setLayoutParams(params);
//
//        ViewGroup parent = (ViewGroup) findViewById(R.id.parent);
//        parent.addView(image, 1);
//
//        if (cancelPotetialWork(R.drawable.image5, image)) {
//            BitmapWorkerTask task = new BitmapWorkerTask(image);
//
//            Bitmap placeHolder = decodeSampleBitmapFromResource(getResources(), R.drawable.image2, 100, 100);
//            final AsyncDrawable drawable = new AsyncDrawable(getResources(), placeHolder, task);
//            image.setImageDrawable(drawable);
//            task.execute(R.drawable.image5);
//        }
    }


    private boolean cancelPotetialWork(int resId, ImageView image) {
        BitmapWorkerTask task = getBitmapWorkerTask(image);

        if (null != task) {
            if (0 == task.data || resId != task.data)
                task.cancel(true);
            else
                return false;
        }
        return true;
    }


    private BitmapWorkerTask getBitmapWorkerTask(ImageView image) {
        final BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();

        if (null != drawable && drawable instanceof AsyncDrawable) {
            WeakReference<BitmapWorkerTask> ref = ((AsyncDrawable) drawable).getWorkerReference();

            BitmapWorkerTask task = null;
            if (null != ref && null != ref.get()) {
                task = ref.get();
            }
            return task;
        }
        return null;
    }


    private static Bitmap decodeSampleBitmapFromResource(Resources res, int resource, int width, int height) {
        DisplayMetrics metrics = res.getDisplayMetrics();

        final int dim1 = (int) (width * metrics.density);
        final int dim2 = (int) (height * metrics.density);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(res, resource, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateBitmapSize(options, dim1, dim2);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resource, options);
    }



    private static int calculateBitmapSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        final int width = options.outWidth / 2;
        final int height = options.outHeight / 2;

        while (reqWidth < (width / inSampleSize) && reqHeight < (height / inSampleSize))
            inSampleSize <<= 1;
        return inSampleSize;
    }


    private class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> lazyImage;

        private int data;

        public BitmapWorkerTask(ImageView image) {
            lazyImage = new WeakReference<ImageView>(image);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return decodeSampleBitmapFromResource(getResources(), params[0], 200, 200);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled())
                return;

            if (null != bitmap && null != lazyImage) {
                ImageView image = lazyImage.get();

                BitmapWorkerTask task = getBitmapWorkerTask(image);
                if (this == task && null != image)
                    image.setImageBitmap(bitmap);
            }
        }
    }

    private class AsyncDrawable extends BitmapDrawable {
        private WeakReference<BitmapWorkerTask> workerReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask task) {
            super(res, bitmap);
            workerReference = new WeakReference<BitmapWorkerTask>(task);
        }

        public WeakReference<BitmapWorkerTask> getWorkerReference() {
            return workerReference;
        }
    }
}
