package com.jvra.animation.atrs;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import com.jvra.animation.R;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by Jansel R. Abreu (Vanwolf) oan 8/6/2014.
 */
public class AttributeSetCrawler extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attributes);
    }

    public void crawl( View v ){
        XmlPullParser parser = getResources().getXml( R.xml.mobalytics );
        AttributeSet set = Xml.asAttributeSet(parser);

        Log.e( "****",set.getAttributeBooleanValue(null,"active",false)+"" );
        Log.e( "****",set.getAttributeIntValue(null,"max_time",-1)+"" );
        Log.e( "****",set.getAttributeValue(null,"com.class")+"" );
    }
}
