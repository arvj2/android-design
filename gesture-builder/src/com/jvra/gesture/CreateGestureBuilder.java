package com.jvra.gesture;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class CreateGestureBuilder extends Activity {

    private static final float LENGTH_THRESHOLD = 120.f;

    private Gesture gesture;
    private View doneButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_gesture);


        doneButton = findViewById(R.id.done);
        GestureOverlayView overlay = ( GestureOverlayView ) findViewById( R.id.gesture_overlay );
        overlay.addOnGestureListener( new GestureProcessor() );
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if( null != gesture )
            outState.putParcelable("gesture",gesture);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gesture = savedInstanceState.getParcelable("gesture");
        if( null != gesture ){
            final GestureOverlayView overlay = ( GestureOverlayView ) findViewById( R.id.gesture_overlay );
            overlay.post(new Runnable() {
                @Override
                public void run() {
                    overlay.setGesture(gesture);
                }
            });
        }
        doneButton.setEnabled(true);
    }


    public void addGesture(View view){
        if( null != gesture ){
            final TextView  input = (TextView) findViewById( R.id.gesture_name );
            final CharSequence name = input.getText();
            if( name.length() == 0){
                input.setError(getString(R.string.error_missing_name));
                return;
            }

            final GestureLibrary store = GestureBuilderActivity.store();
            store.addGesture(name.toString(),gesture);
            store.save();

            setResult(RESULT_OK);

            final String path = new File(Environment.getExternalStorageDirectory(),"gestures").getAbsolutePath();
            Toast.makeText(this,getString(R.string.save_success,path),Toast.LENGTH_LONG).show();
        }else{
            setResult(RESULT_CANCELED);
        }
        finish();
    }


    public void cancelGesture(View view){
        setResult(RESULT_CANCELED);
        finish();
    }



    private class GestureProcessor implements GestureOverlayView.OnGestureListener{
        @Override
        public void onGestureStarted(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
            doneButton.setEnabled(false);
            gesture = null;
        }

        @Override
        public void onGesture(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        }

        @Override
        public void onGestureEnded(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
            gesture = gestureOverlayView.getGesture();
            if( gesture.getLength() < LENGTH_THRESHOLD )
                gestureOverlayView.clear(false);
            doneButton.setEnabled(true);
        }

        @Override
        public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

        }
    }
}
