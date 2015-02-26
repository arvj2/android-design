package com.jvra.demos.media;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.jvra.demos.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/16/2014.
 */
public class AudioFxDemo extends Activity {

    private static final String TAG = "AudioFxDemo";
    private static float VISUALIZER_HEIGHT_DIP = 50f;


    private MediaPlayer player;
    private Visualizer visualizer;
    private Equalizer equalizer;

    private LinearLayout linearLayout;
    private VisualizerView visualizerView;
    private TextView statusView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        statusView = new TextView(this);

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(statusView);

        setContentView(linearLayout);

        player = MediaPlayer.create(this, R.raw.test_cbr );
        Log.e(TAG, "MediaPlayer audio session ID: " + player.getAudioSessionId());

        setupVisualizerFxAndUI();
        setupEqualizerFxAndUI();

        visualizer.setEnabled(true);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                visualizer.setEnabled(false);
            }
        });

        player.start();
        statusView.setText( "Playing audio" );

    }


    private void setupEqualizerFxAndUI(){
        equalizer = new Equalizer(0,player.getAudioSessionId());
        equalizer.setEnabled(true);

        TextView eq = new TextView(this);
        eq.setText("Equalizer: ");
        linearLayout.addView(eq);

        short bands = equalizer.getNumberOfBands();
        final short  minLevel = equalizer.getBandLevelRange()[0];
        final short  maxLevel = equalizer.getBandLevelRange()[1];

        for (short i = 0; i < bands; i++) {
            final short band = i;
            TextView freq = new TextView(this);
            freq.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            freq.setGravity(Gravity.CENTER_HORIZONTAL);
            freq.setText(equalizer.getCenterFreq(band) /100 +"Hz" );
            linearLayout.addView(freq);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView minDbTextView = new TextView(this);
            minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            minDbTextView.setText((minLevel / 100) + " dB");

            TextView maxDbTextView = new TextView(this);
            maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            maxDbTextView.setText((maxLevel / 100) + " dB");

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            params.weight = 1;
            SeekBar bar = new SeekBar(this);
            bar.setLayoutParams(params);
            bar.setMax(maxLevel-minLevel);
            bar.setProgress(equalizer.getBandLevel(band));


            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    equalizer.setBandLevel(band,(short)(progress+minLevel));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            row.addView(minDbTextView);
            row.addView(bar);
            row.addView(maxDbTextView);

            linearLayout.addView(row);
        }
    }

    private void setupVisualizerFxAndUI(){
        visualizerView = new VisualizerView(this);
        visualizerView.setLayoutParams( new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int)(VISUALIZER_HEIGHT_DIP*getResources().getDisplayMetrics().density)));
        linearLayout.addView(visualizerView);

        visualizer = new Visualizer(player.getAudioSessionId());
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {
                visualizerView.updateVisualizer(bytes);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {

            }
        }, Visualizer.getMaxCaptureRate()/2,true,false);
    }


    private class VisualizerView extends View {
        private byte[] bytes;
        private float[] points;
        private Rect rect = new Rect();

        private Paint forePaint = new Paint();

        public VisualizerView(Context context) {
            super(context);
            init();
        }

        private void init() {
            bytes = null;
            forePaint.setStrokeWidth(1f);
            forePaint.setAntiAlias(true);
            forePaint.setColor(Color.rgb(0, 128, 255));
        }

        public void updateVisualizer(byte[] bytes) {
            this.bytes = bytes;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (bytes == null) {
                return;
            }

            if (points == null || points.length < bytes.length * 4) {
                points = new float[bytes.length * 4];
            }

            rect.set(0, 0, getWidth(), getHeight());

            for (int i = 0; i < bytes.length - 1; i++) {
                points[i * 4] = rect.width() * i / (bytes.length - 1);
                points[i * 4 + 1] = rect.height() / 2 + (byte) (bytes[i] + 128) * (rect.height() / 2) / 128;
                points[i * 4 + 2] = rect.width() * (i + 1) / (bytes.length - 1);
                points[i * 4 + 3] = rect.height() / 2 + ((byte) (bytes[i + 1] + 128)) * (rect.height() / 2) / 128;
            }
            canvas.drawLines(points, forePaint);
        }
    }
}

