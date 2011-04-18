package com.lenovo.microlog;

import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class SinaMicrolog extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initializeTabs();
         
        MainActivity.Uri = this.getIntent().getData();
        
        Message m = Message.obtain();
        m.what = MainActivity.AUTHEND;
        Broker.instance.post(OAuthWrap.OAuthTransceiver, m);
   }

    private void initializeTabs()
    {
        try
        {
	        /** TabHost will have Tabs */
	        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
	
	        /** TabSpec used to create a new tab.
	        * By using TabSpec only we can able to setContent to the tab.
	        * By using TabSpec setIndicator() we can set name to tab. */
	
//	        TabSpec tabAuth = tabHost.newTabSpec("tid1");
	        TabSpec tabTimelines = tabHost.newTabSpec("tid1");
	
	        /** TabSpec setIndicator() is used to set name for the tab. */
	        /** TabSpec setContent() is used to set content for a particular tab. */
//	        tabAuth.setIndicator("Authentication").setContent(new Intent(this, OAuthActivity.class));
	        tabTimelines.setIndicator("Timelines").setContent(new Intent(this, Timelines.class));
	
	        /** Add tabSpec to the TabHost to display. */
//	        tabHost.addTab(tabAuth);
	        tabHost.addTab(tabTimelines);
	        tabHost.setCurrentTab(0);
	        
//	    	RadioGroup radioTimelines = (RadioGroup) findViewById(R.id.main_radio);
//	    	radioTimelines.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//				
//				@Override
//				public void onCheckedChanged(RadioGroup group, int checkedId) {
//			        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
//					switch (checkedId)
//					{
//					case R.id.radio1:
//						tabHost.setCurrentTab(0);
//						break;
//					case R.id.radio2:
//						tabHost.setCurrentTab(1);
//						break;
//					default:
//						break;
//					}
//				}
//			});
        }
        catch (Exception ex)
        {
        	System.out.println("Failed to get timeline: " + ex.getMessage());
        }
    }
}
