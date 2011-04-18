package com.lenovo.microlog;

import java.util.List;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Timelines extends Activity  implements Callback {
	
	public final static String TAG = "TimelinesActivity";
	public final static int WHAT = 1234;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timelines);

		OAuthWrap.OAuthTimelines = Broker.instance.subscribe(new Handler(this));
    }
    
    public void Update(String user, String password) {
        try {
            // Other methods require authentication
            Weibo weibo = new Weibo(user, password);
            
            List<Status> statuses = weibo.getPublicTimeline();
            //statuses = weibo.getFriendsTimeline();
            //statuses = weibo.getUserTimeline("1230928585");
            
            int i = 0;
            arrTimelines = new String[statuses.size()];
            for (Status status : statuses) {
                System.out.println(status.getUser().getName() + ": " +
                                   status.getText());
                arrTimelines[i++] = status.getUser().getName() + ": \n" +
                					status.getText();
            }
            
    		lvTimelines = (ListView)findViewById(R.id.lvTimelines);
    		// By using setAdpater method in listview we an add string array in list.
    		lvTimelines.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrTimelines));

        } catch (WeiboException te) {
            System.out.println("Failed to get timeline: " + te.getMessage());
        }
    }
    
    public void UpdateEx() {
        try {
			Weibo weibo = OAuthConstant.getInstance().getWeibo();
			weibo.setToken(OAuthConstant.getInstance().getToken(), OAuthConstant.getInstance().getTokenSecret());
			List<Status> friendsTimeline;
			try {
				friendsTimeline = weibo.getFriendsTimeline();
	            int i = 0;
	            arrTimelines = new String[friendsTimeline.size()];
	            for (Status status : friendsTimeline) {
	                System.out.println(status.getUser().getScreenName() + ": " +
	                                   status.getText());
	                arrTimelines[i++] = status.getUser().getScreenName() + ": \n" +
	                					status.getText();
	            }
			} catch (WeiboException e) {
				e.printStackTrace();
			}
            
			Message msg = Message.obtain();
			msg.what = WHAT;
			Broker.instance.post(OAuthWrap.OAuthTimelines, msg);
			
        } catch (Exception te) {
            System.out.println("Failed to get timeline: " + te.getMessage());
        }
    }

	 @Override
	 public boolean handleMessage(Message msg) {
         switch(msg.what){
         case OAuthTransceiver.AUTHSUCCESS:
                Log.i(TAG, "Received message from " + OAuthTransceiver.TAG);
    			AccessToken accessToken = OAuthConstant.getInstance().getAccessToken();
    			
    			String m = "得到AccessToken的key和Secret,可以使用这两个参数进行授权登录了.\n Access token:\n"+accessToken.getToken()+"\n Access token secret:\n"+accessToken.getTokenSecret();
    			Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    			
    			Thread th = new Thread() {
    				public void run()
    				{
    					UpdateEx();
    				}
    			};
    			th.start();
    			
                break;
                
         case Timelines.WHAT:
     		lvTimelines = (ListView)findViewById(R.id.lvTimelines);
    		// By using setAdpater method in listview we an add string array in list.
    		lvTimelines.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrTimelines));
    		break;
         }
         return true;
	 }
    
    private ListView lvTimelines;
    private String arrTimelines[] = {};
}
