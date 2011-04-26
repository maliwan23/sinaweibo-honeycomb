package com.lenovo.dll.SinaWeibo;

//package weibo4andriod.andriodexamples;

import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AndriodExample extends Activity {
	
	static final String TAG = "Handler";
	static final int HANDLER_TEST = 1;
	
	Handler h = new Handler(){
		public void handleMessage (Message msg)
		{
			switch(msg.what)
			{
			case HANDLER_TEST:
				Log.d(TAG, "The handler thread id = " + Thread.currentThread().getId() + "\n");
			}
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		setContentView(R.layout.main);
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	Button beginOuathBtn=(Button) findViewById(R.id.Button_Load);
    	

    	beginOuathBtn.setOnClickListener(new Button.OnClickListener()
        {

            //@Override
            public void onClick( View v )
            {
                new myThread().start();
            }
        } );
        
	}
	
    class myThread extends Thread
    {
    	public void run()
    	{
    		Message msg = new Message();
    		msg.what = HANDLER_TEST;
    		
        	Weibo weibo = OAuthConstant.getInstance().getWeibo();
        	RequestToken requestToken;
			try {
				requestToken =weibo.getOAuthRequestToken("weibo4andriod://OAuthActivity");
    			Uri uri = Uri.parse(requestToken.getAuthenticationURL()+ "&from=xweibo");
    			OAuthConstant.getInstance().setRequestToken(requestToken);
    			startActivity(new Intent(Intent.ACTION_VIEW, uri));
			} catch (WeiboException e) {
				e.printStackTrace();
			}
    		
    		h.sendMessage(msg);
    		Log.d(TAG, "The worker thread id = " + Thread.currentThread().getId() + "\n");  		
    	}
    }
}