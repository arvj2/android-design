package com.jvra.demos.content;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.jvra.demos.R;

import java.util.Arrays;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/15/2014.
 */
public class ClipboardDimmies extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clipboard_dummies);

        final ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cm.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData data = cm.getPrimaryClip();
                Log.e( "********",data.toString() );
                ClipDescription desc = data.getDescription();
                Log.e("********", Arrays.toString( desc.filterMimeTypes("text/*") ) );
                Log.e( "********",desc.toString() );
                Log.e( "***********",data.getItemCount() +"");
                ClipData.Item i = data.getItemAt(0);
                Log.e( "******",i.coerceToText(ClipboardDimmies.this)+"" );

            }
        });
    }

    public void onClick( View view ){
        final ClipboardManager cm = ( ClipboardManager ) getSystemService( CLIPBOARD_SERVICE );

        ClipData.Item text = new ClipData.Item("First item.text");
        ClipData.Item uri = new ClipData.Item(Uri.parse("content://long/25"));
        ClipData.Item intent = new ClipData.Item( new Intent(this,ClipboardSample.class));

        ClipData data = new ClipData( "This is the first clipboard",new String[]{"application/json","plain/text","text/xml","content/uri","text/html"},text );
        data.addItem(uri);
        data.addItem(intent);

        cm.setPrimaryClip(data);
    }
}
