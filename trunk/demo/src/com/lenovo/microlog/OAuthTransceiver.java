package com.lenovo.microlog;

import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class OAuthTransceiver extends Transceiver {
    public final static String TAG="OAuthTransceiver";
    public final static int WHAT = 3862; // a magic number
	public final static int REDIRECT_VIEW = 3864; // a magic number
 	public final static int AUTHSUCCESS = 3866; // a magic number
   
    public boolean handleMessage(Message m){
            
            switch(m.what){
            case OAuthTask.WHAT:
                    Log.i(TAG,"Received message from " + OAuthTask.TAG);
                    // attention: when answering we need a new message
                    Message msg= Message.obtain(m);
                    Broker.instance.post(OAuthWrap.OAuthBroker, msg);
                    break;
            case MainActivity.WHAT:
                    Log.i(TAG,"Received message from " + MainActivity.TAG );
                    if (oauthBegin())
                    {
                    	Message ms = Message.obtain();
                    	ms.what = REDIRECT_VIEW;
                    	Broker.instance.post(OAuthWrap.OAuthActivity, ms);
                    }
                    break;
            case MainActivity.AUTHEND:
                    Log.i(TAG,"Received message from " + MainActivity.TAG );
                    if (!oauthEnd())
                    {
//                    	Message ms = Message.obtain();
//                    	ms.what = AUTHFAIL;
//                    	Broker.instance.post(OAuthWrap.OAuthActivity, ms);
                    }
                    break;
            }
            return true;
    }
    
    private boolean oauthBegin()
    {
    	Weibo weibo = OAuthConstant.getInstance().getWeibo();
    	RequestToken requestToken;
		try {
			requestToken =weibo.getOAuthRequestToken("weibo4andriod://OAuthActivity");
			MainActivity.Uri = Uri.parse(requestToken.getAuthenticationURL()+ "&from=xweibo");
			OAuthConstant.getInstance().setRequestToken(requestToken);
//			startActivity(new Intent(Intent.ACTION_VIEW, uri));
			return true;
		} catch (WeiboException e) {
			e.printStackTrace();
			return false;
		}
    }
    
    private boolean oauthEnd()
    {
    	try {
			RequestToken requestToken = OAuthConstant.getInstance().getRequestToken();
			AccessToken accessToken = requestToken.getAccessToken(MainActivity.Uri.getQueryParameter("oauth_verifier"));
			OAuthConstant.getInstance().setAccessToken(accessToken);
			
        	Message m = Message.obtain();
        	m.what = AUTHSUCCESS;
        	Broker.instance.post(OAuthWrap.OAuthTimelines, m);

			return true;
		} catch (WeiboException e) {
			e.printStackTrace();
			return false;
		}
    }
}
