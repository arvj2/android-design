package com.jvra.gesture;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.util.*;

/**
 * Created by Jansel Valentin R. (jrodr) on 01/13/15.
 */
public class GestureBuilderActivity extends ListActivity{

    private static final int STATUS_SUCCESS = 0;
    private static final int STATUS_CANCELED = 1;
    private static final int STATUS_NO_STORAGE = 2;
    private static final int STATUS_NOT_LOADED = 3;

    private static final int MENU_ID_RENAME = 1;
    private static final int MENU_ID_REMOVE = 2;

    private static final int DIALOG_RENAME_GESTURE = 1;

    private static final int REQUEST_NEW_GESTURE = 1;

    private static final String GESTURES_INFO_ID = "gestures.info_id";

    private final File storeFile = new File(Environment.getExternalStorageDirectory(),"gestures");

    private final Comparator<NamedGesture> sorter = new Comparator<NamedGesture>() {
        @Override
        public int compare(NamedGesture lhs, NamedGesture rhs) {
            return lhs.name.compareTo(rhs.name);
        }
    };


    private static GestureLibrary store;
    private GestureAdapter adapter;
    private GestureLoadTask task;
    private TextView empty;

    private Dialog renameDialog;
    private EditText input;
    private NamedGesture currentRenameGesture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.gestures_list );

        adapter = new GestureAdapter(this);
        setListAdapter(adapter);
        if( null == store )
            store = GestureLibraries.fromFile(storeFile);

        empty = (TextView) findViewById(R.id.empty);
        loadGestures();

        registerForContextMenu(getListView());
    }

    static GestureLibrary store(){
        return store;
    }

    public void reloadGestures(View view){
        loadGestures();
    }


    public void addGesture(View view){
        Intent intent = new Intent(this,CreateGestureBuilder.class);
        startActivityForResult(intent,REQUEST_NEW_GESTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( RESULT_OK == resultCode ){
            if( REQUEST_NEW_GESTURE == requestCode )
                loadGestures();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( null != task && task.getStatus() != GestureLoadTask.Status.FINISHED ) {
            task.cancel(true);
            task = null;
        }
        cleanupRenameDialog();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if( currentRenameGesture != null )
            outState.putLong(GESTURES_INFO_ID, currentRenameGesture.gesture.getID());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        long id = state.getLong(GESTURES_INFO_ID,-1);
        if( -1 != id ){
            final Set<String> entries = store.getGestureEntries();
out:            for( String name : entries ){
                for( Gesture gesture : store.getGestures(name) ){
                    if( gesture.getID() == id ){
                        currentRenameGesture = new NamedGesture();
                        currentRenameGesture.name = name;
                        currentRenameGesture.gesture = gesture;
                        break out;
                    }
                }
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(((TextView) info.targetView).getText());

        menu.add(0,MENU_ID_RENAME,0,R.string.gestures_rename);
        menu.add(0,MENU_ID_REMOVE,0,R.string.gestures_delete);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final NamedGesture gesture = (NamedGesture) menuInfo.targetView.getTag();

        if( MENU_ID_REMOVE == item.getItemId() ){
            renameGesture(gesture);
            return true;
        }
        if( MENU_ID_REMOVE == item.getItemId() ){
            deleteGesture(gesture);
            return true;
        }
        return super.onContextItemSelected(item);
    }


    private void renameGesture(NamedGesture gesture){
        currentRenameGesture = gesture;
        showDialog(DIALOG_RENAME_GESTURE);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if( DIALOG_RENAME_GESTURE == id )
            return createRenameDialog();
        return super.onCreateDialog(id);
    }


    private Dialog createRenameDialog(){
        final View layout = View.inflate(this,R.layout.dialog_rename,null);
        input = (EditText) layout.findViewById(R.id.name);
        ((TextView) findViewById(R.id.label)).setText(R.string.gestures_rename_label);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(0)
                .setTitle(getString(R.string.gestures_rename_title))
                .setCancelable(true)
                .setOnCancelListener(new Dialog.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cleanupRenameDialog();
                    }
                })
                .setNegativeButton(getString(R.string.cancel_action),new Dialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cleanupRenameDialog();
                    }
                })
                .setPositiveButton(getString(R.string.rename_action), new Dialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeGestureName();
                    }
                })
                .setView(layout);

        return builder.create();

    }


    private void deleteGesture(NamedGesture gesture){
        store.removeGesture(gesture.name, gesture.gesture);
        store.save();

        adapter.setNotifyOnChange(false);
        adapter.remove(gesture);
        adapter.sort(sorter);
        checkForEmpty();
        adapter.notifyDataSetChanged();

        Toast.makeText(this, R.string.gestures_delete_success, Toast.LENGTH_SHORT).show();
    }


    private void changeGestureName(){
        final String name = input.getText().toString();
        if(!TextUtils.isEmpty(name)){
            final NamedGesture renameGesture = currentRenameGesture;
            final GestureAdapter lAdapter = adapter;

            final int count = adapter.getCount();

            for( int i=0;count>i;++i ){
                final NamedGesture gesture = adapter.getItem(i);
                if( gesture.gesture.getID() == renameGesture.gesture.getID() ){
                    store.removeGesture( gesture.name,gesture.gesture );
                    gesture.name = input.getText().toString();
                    store.addGesture(gesture.name,gesture.gesture);
                    break;
                }
            }
            lAdapter.notifyDataSetChanged();
        }

        currentRenameGesture = null;
    }


    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        if (DIALOG_RENAME_GESTURE == id) {
            input.setText(currentRenameGesture.name);
        }
    }

    private void cleanupRenameDialog(){
        if( null != renameDialog ){
            renameDialog.dismiss();
            renameDialog = null;
        }
        currentRenameGesture = null;
    }


    private void loadGestures(){
        if( null != task && task.getStatus() != GestureLoadTask.Status.FINISHED ){
            task.cancel(true);
        }
        task = (GestureLoadTask) new GestureLoadTask().execute();
    }


    private void checkForEmpty(){
        if( 0 == adapter.getCount() )
            empty.setText( R.string.gestures_empty );
    }



    private class GestureLoadTask extends AsyncTask<Void,NamedGesture,Integer>{
        private int thumbSize;
        private int thumbInset;
        private int pathColor;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            final Resources res = getResources();
            pathColor = res.getColor( R.color.gesture_color );
            thumbSize = (int)res.getDimension(R.dimen.gesture_thumbnail_size);
            thumbInset = (int)res.getDimension(R.dimen.gesture_thumbnail_inset);

            findViewById( R.id.addButton ).setEnabled(false);
            findViewById( R.id.reloadButton ).setEnabled(false);

            adapter.setNotifyOnChange(false);
            adapter.clear();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if(isCancelled()) return STATUS_CANCELED;
            if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                return STATUS_NO_STORAGE;

            final GestureLibrary lStore = store;
            if(lStore.load()){
                for(String entry : lStore.getGestureEntries() ){
                    if(isCancelled())
                        break;
                    for(Gesture gesture : store.getGestures(entry)){
                        final Bitmap bitmap = gesture.toBitmap(thumbSize,thumbSize,thumbInset,pathColor);
                        final NamedGesture namedGesture = new NamedGesture();
                        namedGesture.gesture = gesture;
                        namedGesture.name = entry;
                        adapter.addBitmap(namedGesture.gesture.getID(),bitmap);
                        publishProgress(namedGesture);
                    }
                }
                return STATUS_SUCCESS;
            }
            return STATUS_NOT_LOADED;
        }

        @Override
        protected void onProgressUpdate(NamedGesture... values) {
            super.onProgressUpdate(values);

            final GestureAdapter lAdapter = adapter;
            lAdapter.setNotifyOnChange(false);

            for( NamedGesture gesture : values ){
                lAdapter.add(gesture);
            }

            adapter.sort(sorter);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Integer status) {
            super.onPostExecute(status);
            if( STATUS_NO_STORAGE == status ){
                getListView().setVisibility( View.GONE );
                empty.setVisibility(View.VISIBLE);
                empty.setText( getString(R.string.gestures_error_loading,storeFile.getAbsoluteFile()));
            }else{
                findViewById( R.id.addButton ).setEnabled(true);
                findViewById( R.id.reloadButton).setEnabled(true);
                checkForEmpty();
            }
        }
    }



    private class NamedGesture{
        private Gesture gesture;
        private String name;
    }


    private class GestureAdapter extends ArrayAdapter<NamedGesture>{

        private final LayoutInflater inflater;
        private final Map<Long,Drawable> thumbs = Collections.synchronizedMap(new HashMap<Long, Drawable>());


        public GestureAdapter(Context context) {
            super(context, 0);
            inflater = LayoutInflater.from(context);
        }

        private void addBitmap(Long id,Bitmap bitmap ){
            thumbs.put(id,new BitmapDrawable(getResources(),bitmap));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if( null == convertView )
                convertView = inflater.inflate(R.layout.gesture_item,parent,false);

            final NamedGesture namedGesture = getItem(position);
            final TextView label = (TextView) convertView;

            label.setTag(namedGesture);
            label.setText(namedGesture.name);
            label.setCompoundDrawablesWithIntrinsicBounds(thumbs.get(namedGesture.gesture.getID()),null,null,null);
            return convertView;
        }
    }
}





























