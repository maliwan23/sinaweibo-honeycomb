package com.lenovo.microlog;

import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity implements Callback {
	
	public final static String TAG = "OAuthActivity";
	public final static int WHAT = 34834; 	// a magic number
	public final static int AUTHEND = 34835; // a magic number
	public static Uri Uri;
	private OAuthTask mOAuthBroker;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);
        
		mOAuthBroker = new OAuthTask();
		OAuthWrap.OAuthTransceiver = Broker.instance.subscribe(new OAuthTransceiver().getHandler());
		// additionally, we can use activities (or other components with loopers)
		// mind that our activity implements Callback
		OAuthWrap.OAuthActivity = Broker.instance.subscribe(new Handler(this));

		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	
    	Button beginOAuthBtn = (Button) findViewById(R.id.btnAuth);
    	beginOAuthBtn.setOnClickListener( new Button.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
            	Message m = Message.obtain();
            	m.what = WHAT;
            	Broker.instance.post(OAuthWrap.OAuthTransceiver, m);
            }
        } );
    	
    	Button floatPanelBtn = (Button) findViewById(R.id.btnFloatPanel);
    	floatPanelBtn.setOnClickListener( new Button.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
            	try
            	{
	            	View view = View.inflate(getApplicationContext(), R.layout.floatpanel, null);
	            	
	                WindowManager wm = (WindowManager)getApplicationContext().getSystemService("window");

	                WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
	                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
	                wmParams.format = 1;
	                wmParams.x = 400;
	                wmParams.y = 0;
	                wmParams.width = 300;
	                wmParams.height = 600;
	                wmParams.alpha = 100;
	                
	                wm.addView(view, wmParams);
	                
	                finish();
            	}
            	catch (Exception ex)
            	{
            		Log.e("Add View", ex.toString());
            	}
            }
        } );
    }
    
    @Override
    public void onResume(){
    	try
    	{
    		super.onResume();
    	}
    	catch (Exception ex)
    	{
    		Log.e("onResume", ex.toString());
    	}
    }

	 @Override
	 public void onStart(){
	     super.onStart();
	     mOAuthBroker.onStart();
	     OAuthWrap.OAuthBroker = mOAuthBroker.getBrokerAddress();
	 }

	 @Override 
	 public void onDestroy(){
	     super.onDestroy();
	     mOAuthBroker.onQuit();
	 }

	 @Override
	 public boolean handleMessage(Message msg) {
         switch(msg.what){
         case OAuthTransceiver.REDIRECT_VIEW:
                 Log.i(TAG,"Received message from " + OAuthTransceiver.TAG);
                 startActivity(new Intent(Intent.ACTION_VIEW, Uri));
                 break;
         }
         return true;
	 }
}
