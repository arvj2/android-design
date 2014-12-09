package com.jvra.demos.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.accessibility.AccessibilityEvent;
import com.jvra.demos.R;

import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/8/2014.
 */
public class ClockBackService extends AccessibilityService{

    private static final String LOG_TAG = ClockBackService.class.getSimpleName();

    private static final int EVENT_NOTIFICATION_TIMEOUT_MILLIS = 80;

    private static final String[] PACKAGE_NAMES = new String[] {
            "com.android.alarmclock", "com.google.android.deskclock", "com.android.deskclock"
    };

    private static  final int MESSAGE_SPEAK = 1;
    private static  final int MESSAGE_STOP_SPEAK = 2;
    private static  final int MESSAGE_START_TTS = 3;
    private static  final int MESSAGE_SHUTDOWN_TTS = 4;
    private static  final int MESSAGE_PLAY_EARCON = 5;
    private static  final int MESSAGE_STOP_PLAY_EARCON = 6;
    private static  final int MESSAGE_VIBRATE = 7;
    private static  final int MESSAGE_STOP_VIBRATE = 8;

    private static final int INDEX_SCREEN_ON      = 0x00000100;
    private static final int INDEX_SCREEN_OFF     = 0x00000200;
    private static final int INDEX_RINGER_NORMAL  = 0x00000400;
    private static final int INDEX_RINGER_VIBRATE = 0x00000800;
    private static final int INDEX_RINGER_SILENT  = 0x00001000;

    private static final int QUEUING_MODE_INTERRUPT = 2;

    private static final String SPACE = " ";

    private static final SparseArray<long[]> sVibrationPatterns = new SparseArray<long[]>();
    static {
        sVibrationPatterns.put(AccessibilityEvent.TYPE_VIEW_CLICKED,new long[]{0L,100L});
        sVibrationPatterns.put(AccessibilityEvent.TYPE_VIEW_LONG_CLICKED,new long[]{0L,100L});
        sVibrationPatterns.put(AccessibilityEvent.TYPE_VIEW_SELECTED,new long[]{0L,15L,10L,15L});
        sVibrationPatterns.put(AccessibilityEvent.TYPE_VIEW_FOCUSED,new long[]{0L,15L,10L,15L});
        sVibrationPatterns.put(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,new long[]{0L,25L,50L,25L,50L,25L});
        sVibrationPatterns.put(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER,new long[]{0L,15L,10L,15L,15L,10L});
        sVibrationPatterns.put(INDEX_SCREEN_ON,new long[]{0L,10L,10L,20L,20L,30L});
        sVibrationPatterns.put(INDEX_SCREEN_OFF,new long[]{0L,30L,20L,20L,10L,10L});
    }

    private static final SparseArray<Integer> sSoundResourceIds = new SparseArray<Integer>();
    static{
        sSoundResourceIds.put(AccessibilityEvent.TYPE_VIEW_CLICKED, R.raw.sound_view_clicked);
        sSoundResourceIds.put(AccessibilityEvent.TYPE_VIEW_LONG_CLICKED, R.raw.sound_view_clicked);
        sSoundResourceIds.put(AccessibilityEvent.TYPE_VIEW_SELECTED, R.raw.sound_view_focused_or_selected);
        sSoundResourceIds.put(AccessibilityEvent.TYPE_VIEW_FOCUSED, R.raw.sound_view_focused_or_selected);
        sSoundResourceIds.put(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED, R.raw.sound_window_state_changed);
        sSoundResourceIds.put(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER, R.raw.sound_view_hover_enter);
        sSoundResourceIds.put(INDEX_SCREEN_ON, R.raw.sound_screen_on);
        sSoundResourceIds.put(INDEX_SCREEN_OFF, R.raw.sound_screen_off);
        sSoundResourceIds.put(INDEX_RINGER_SILENT, R.raw.sound_ringer_silent);
        sSoundResourceIds.put(INDEX_RINGER_VIBRATE, R.raw.sound_ringer_vibrate);
        sSoundResourceIds.put(INDEX_RINGER_NORMAL, R.raw.sound_ringer_normal);
    }

    private final SparseArray<String> mEarconNames = new SparseArray<String>();

    Context context;

    int proviedFeedbackType;

    private final StringBuilder mUuterance = new StringBuilder();

    private TextToSpeech mTts;

    private AudioManager audioManager;

    private Vibrator vibrator;

    private boolean isInfraestructureInitialized;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch( msg.what){
                case MESSAGE_SPEAK:
                    String utterance = (String) msg.obj;
                    mTts.speak(utterance,QUEUING_MODE_INTERRUPT,null);
                    return;
                case MESSAGE_STOP_SPEAK:
                    mTts.stop();
                    return;
                case MESSAGE_START_TTS:
                   mTts = new TextToSpeech(context,new TextToSpeech.OnInitListener(){
                       @Override
                       public void onInit(int i) {
                          registerBroadCastReceiver();
                       }
                   });
                   return;
                case MESSAGE_SHUTDOWN_TTS:
                    mTts.stop();
                    return;
                case MESSAGE_PLAY_EARCON:
                    int resourceId = msg.arg1;
                    playEarcon(resourceId);
                    return;
                case MESSAGE_STOP_PLAY_EARCON:
                    mTts.stop();
                    return;
                case MESSAGE_VIBRATE:
                    int key = msg.arg1;
                    long[] pattern = sVibrationPatterns.get(key);
                    if (pattern != null) {
                        vibrator.vibrate(pattern, -1);
                    }
                    return;
                case MESSAGE_STOP_VIBRATE:
                    vibrator.cancel();
                    return;
            }
        }
    };


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if( AudioManager.RINGER_MODE_CHANGED_ACTION.equals(action)){
                int ringerMode = intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE,AudioManager.RINGER_MODE_NORMAL);
                configuForRingerMode(ringerMode);
            }else if( Intent.ACTION_SCREEN_ON.equals(action))
                provideScreenStateChangeFeedback(INDEX_SCREEN_ON);
            else if( Intent.ACTION_SCREEN_OFF.equals(action))
                provideScreenStateChangeFeedback(INDEX_SCREEN_OFF);
            else
                Log.w(LOG_TAG,"Registered for but not handling action "+action);
        }
    };


    private void provideScreenStateChangeFeedback(int feedbackIndex){
        switch( proviedFeedbackType ){
            case AccessibilityServiceInfo.FEEDBACK_SPOKEN:
                String utterance = generateScreenOnOrOffUtternace(feedbackIndex);
                mHandler.obtainMessage(MESSAGE_SPEAK,utterance).sendToTarget();
                return;
            case AccessibilityServiceInfo.FEEDBACK_AUDIBLE:
                mHandler.obtainMessage(MESSAGE_PLAY_EARCON,feedbackIndex,0).sendToTarget();
                return;
            case AccessibilityServiceInfo.FEEDBACK_HAPTIC:
                mHandler.obtainMessage(MESSAGE_VIBRATE,feedbackIndex,0).sendToTarget();
                return;
        }
    }


    @Override
    protected void onServiceConnected() {
        if(isInfraestructureInitialized)
            return;

        context = this;
        mHandler.sendEmptyMessage(MESSAGE_START_TTS);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        audioManager = ( AudioManager ) getSystemService( AUDIO_SERVICE );
        int ringerMode = audioManager.getRingerMode();
        configuForRingerMode(ringerMode);
        isInfraestructureInitialized = true;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        if(isInfraestructureInitialized){
            mHandler.sendEmptyMessage(MESSAGE_SHUTDOWN_TTS);

            if( mBroadcastReceiver != null )
                unregisterReceiver(mBroadcastReceiver);

            isInfraestructureInitialized = false;
        }
        return false;
    }



    private String generateScreenOnOrOffUtternace(int feedbackIndex){
        int  resourceId = (INDEX_SCREEN_ON == feedbackIndex) ? R.string.template_screen_off : R.string.template_screen_off;
        String template = context.getString( resourceId );

        int currentRingerVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int maxRingerVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int volumePercent = (100/maxRingerVolume) * currentRingerVolume;

        int adjustment = volumePercent % 10;
        if( 5>adjustment )
            volumePercent -=adjustment;
        else
            volumePercent += (10-adjustment);
        return String.format(template,volumePercent);
    }



    private void configuForRingerMode(int mode){
        if( AudioManager.RINGER_MODE_SILENT == mode ){
            proviedFeedbackType = AccessibilityServiceInfo.FEEDBACK_HAPTIC;


            int flags =   AccessibilityServiceInfo.FEEDBACK_HAPTIC
                        | AccessibilityServiceInfo.FEEDBACK_SPOKEN
                        | AccessibilityServiceInfo.FEEDBACK_AUDIBLE;

            setServiceInfo(flags);
            mHandler.obtainMessage(MESSAGE_PLAY_EARCON,INDEX_RINGER_SILENT,0).sendToTarget();
        }else if( AudioManager.RINGER_MODE_VIBRATE == mode ){
            proviedFeedbackType = AccessibilityServiceInfo.FEEDBACK_AUDIBLE;

            setServiceInfo(AccessibilityServiceInfo.FEEDBACK_AUDIBLE | AccessibilityServiceInfo.FEEDBACK_SPOKEN);

            mHandler.obtainMessage( MESSAGE_PLAY_EARCON, INDEX_RINGER_SILENT, 0 ).sendToTarget();
        }else if( AudioManager.RINGER_MODE_NORMAL == mode ){
            proviedFeedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

            setServiceInfo(AccessibilityServiceInfo.FEEDBACK_SPOKEN);
            mHandler.obtainMessage( MESSAGE_PLAY_EARCON,INDEX_RINGER_NORMAL,0 ).sendToTarget();
        }
    }

    private void setServiceInfo(int feedbackType){
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = feedbackType;
        info.notificationTimeout = EVENT_NOTIFICATION_TIMEOUT_MILLIS;
        info.packageNames = PACKAGE_NAMES;

        setServiceInfo(info);
    }


    private void registerBroadCastReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        registerReceiver(mBroadcastReceiver,filter,null,null);
    }


    private void playEarcon( int resource ){
        String earconName = mEarconNames.get(resource);
        if( null == earconName ){
            Integer resId = sSoundResourceIds.get(resource);
            if( null != resId ){
                earconName = "["+resource+"]";
                mTts.addEarcon(earconName,getPackageName(),resource);
                mEarconNames.put(resource,earconName);
            }
        }
        mTts.playEarcon(earconName,QUEUING_MODE_INTERRUPT,null);
    }


    private String formatUtterance( AccessibilityEvent event ){
        StringBuilder builder = mUuterance;
        builder.setLength(0);

        List<CharSequence> eventText = event.getText();
        if( !eventText.isEmpty() ){
            for(CharSequence text : eventText ){
                if( text.charAt(0) == '0')
                    text = text.subSequence(1,text.length());
                builder.append(text);
                builder.append(SPACE);
            }
            return builder.toString();
        }

        CharSequence contentDescription = event.getContentDescription();
        if( null != contentDescription ){
            builder.append(contentDescription);
            return builder.toString();
        }
        return builder.toString();
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e( LOG_TAG,proviedFeedbackType+" "+event.toString());

        if( proviedFeedbackType == AccessibilityServiceInfo.FEEDBACK_SPOKEN )
            mHandler.obtainMessage(MESSAGE_SPEAK,formatUtterance(event)).sendToTarget();
        else if( proviedFeedbackType == AccessibilityServiceInfo.FEEDBACK_AUDIBLE)
            mHandler.obtainMessage(MESSAGE_PLAY_EARCON,event.getEventType(),0).sendToTarget();
        else if( proviedFeedbackType == AccessibilityServiceInfo.FEEDBACK_HAPTIC )
            mHandler.obtainMessage(MESSAGE_VIBRATE,event.getEventType(),0).sendToTarget();
    }

    @Override
    public void onInterrupt() {
        if( proviedFeedbackType == AccessibilityServiceInfo.FEEDBACK_SPOKEN )
            mHandler.obtainMessage(MESSAGE_STOP_SPEAK).sendToTarget();
        else if( proviedFeedbackType == AccessibilityServiceInfo.FEEDBACK_AUDIBLE)
            mHandler.obtainMessage(MESSAGE_STOP_PLAY_EARCON).sendToTarget();
        else if( proviedFeedbackType == AccessibilityServiceInfo.FEEDBACK_HAPTIC )
            mHandler.obtainMessage(MESSAGE_STOP_VIBRATE).sendToTarget();
    }
}
