package com.lenovo.microlog;

import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
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
        
        View view = this.findViewById(R.layout.authentication);
        
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
                	FloatPanel floatPanel = new FloatPanel(getApplicationContext());
//            		floatPanel.UpdateView(getApplicationContext());
//	                finish();
            	}
            	catch (Exception ex)
            	{
            		Log.e("Add View", ex.toString());
            	}
            }
        } );
    	
    	Button layoutBtn = (Button) findViewById(R.id.btnLayout);
    	layoutBtn.setOnClickListener( new Button.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
            	try {
        	    	WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        	    	Button floatPanelBtn = (Button) findViewById(R.id.btnFloatPanel);
        	    	View view = (View)floatPanelBtn.getParent();
        	    	view = (View)view.getParent();
        	    	view = (View)view.getParent();
        	    	view = (View)view.getParent();
        	    	Object vr = view.getParent();
        	    	
        	    	WindowManager.LayoutParams params = new LayoutParams();
        	    	WindowManager.LayoutParams p = (WindowManager.LayoutParams)view.getLayoutParams();
        	    	
        	    	params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL;
            	    params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        	        params.type = LayoutParams.TYPE_PHONE;
        	        params.format = PixelFormat.RGBA_8888;
        	        params.width = 400;
        	        params.height = 200;
        	        params.alpha = 100;
        	        
        	        wm.updateViewLayout(view, params);
            	} catch (Exception ex) {
            		Log.e(this.toString(), ex.toString());
            	}
            }
        } );
     	
    	Button fragmentBtn = (Button) findViewById(R.id.btnFragment);
    	fragmentBtn.setOnClickListener( new Button.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
            	try {
                    Intent fragment = new Intent(getApplicationContext(),
                    		TutListActivity.class);
                    startActivity(fragment);
            	} catch (Exception ex) {
            		Log.e(this.toString(), ex.toString());
            	}
            }
        } );
     	
    	Button btnVideo = (Button) findViewById(R.id.btnVideo);
    	btnVideo.setOnClickListener( new Button.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
            	try {
                    Intent video = new Intent(getApplicationContext(), VideoTestActivity.class);
                    startActivity(video);
            	} catch (Exception ex) {
            		Log.e(this.toString(), ex.toString());
            	}
            }
        } );
     	
    	Button btnWeibo = (Button) findViewById(R.id.btnWeibo);
    	btnWeibo.setOnClickListener( new Button.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
            	try {
                    Intent fragment = new Intent(getApplicationContext(),
                    		WeiboActivity.class);
                    startActivity(fragment);
            	} catch (Exception ex) {
            		Log.e(this.toString(), ex.toString());
            	}
            }
        } );
   }
    
    public void onContentChanged()
    {
    	super.onContentChanged();

//    	try {
//	    	WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//	    	View view = this.getCurrentFocus();
//	    	view = this.findViewById(R.layout.authentication);
//	    	view = this.getWindow().findViewById(R.layout.authentication);
//	    	
//	    	Button floatPanelBtn = (Button) findViewById(R.id.btnFloatPanel);
//	    	view = (View)floatPanelBtn.getParent();
//	    	view = (View)view.getParent();
//	    	view = (View)view.getParent();
//	    	view = (View)view.getParent();
//	    	
//	    	WindowManager.LayoutParams params = new LayoutParams(); // = (WindowManager.LayoutParams)view.getLayoutParams();
//	    	
//	    	params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL;
//	    	params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
//	        params.type = LayoutParams.TYPE_PHONE;
//	        params.format = PixelFormat.RGBA_8888;
////	        params.width = 300;
////	        params.height = 600;
//	        params.alpha = 100;
//	        
//	        wm.addView(view, params);
//    	} catch (Exception ex) {
//    		Log.e(this.toString(), ex.toString());
//    	}
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
