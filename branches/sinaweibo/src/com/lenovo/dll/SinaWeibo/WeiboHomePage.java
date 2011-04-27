package com.lenovo.dll.SinaWeibo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

public class WeiboHomePage extends Activity {
	
	private ListView listview;
	static final int DOWNLOAD_FINISHED = 1;

	private GetTimelineThread getTimelineThread;
	private WeiboPageAdapter weiboPageAdapter;
	private Context mContext;

	private List<Status> friendsTimeline;
	private List<String> stringTimeline;
	private List<String> profileImageUrlList;
	private List<String> middleImageUrlList;

	private Semaphore sem_timeline;
	private Weibo weibo;
	private static final String TAG= "Weibo";
	private static final String TAG_PIC = "WeiboPic"; 
	
	class GetTimelineThread extends Thread {
	
		public void run(){
			try {

			friendsTimeline = weibo.getFriendsTimeline();
					
			} catch (WeiboException e){
				e.printStackTrace();
			} finally {
				sem_timeline.release();
			}
		}
	}
	
	
	private void processRecievedData(){

		StringBuilder stringBuilder = new StringBuilder("1");
		
		for (Status status : friendsTimeline) {
			stringBuilder.setLength(0);
			stringBuilder.append(status.getUser().getScreenName() + "说:"
					+ status.getText() + "\n");
			Log.d(TAG_PIC, status.getBmiddle_pic().toString());
			stringTimeline.add(stringBuilder.toString());
			Log.d(TAG, stringBuilder.toString());
			
			profileImageUrlList.add(status.getUser().getProfileImageURL().toString());
			
			if ( status.getBmiddle_pic() != null && !status.getBmiddle_pic().toString().equals("") ){
				middleImageUrlList.add(status.getBmiddle_pic().toString());
			} else {
				middleImageUrlList.add("NoPhotos");
			}
			
			Log.d(TAG, status.getUser().getProfileImageURL().toString());
		}
		
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.loading_picture);
    	mContext=getApplicationContext();
        
		stringTimeline = new ArrayList<String>();
		profileImageUrlList = new ArrayList<String>();
		middleImageUrlList = new ArrayList<String>();
    
		getTimelineThread = new GetTimelineThread();
        
        weibo = OAuthConstant.getInstance().getWeibo();
        weibo.setToken(OAuthConstant.getInstance().getToken(), OAuthConstant.getInstance().getTokenSecret());
        
		sem_timeline = new Semaphore(1);
		
		weiboPageAdapter=new WeiboPageAdapter(this);

        Log.d(TAG, "imageview should appear here.");
        
        try{
			sem_timeline.acquire();
			getTimelineThread.start();
        } catch (Exception e){
			e.printStackTrace();
		}	

		listview = new ListView(mContext);
		
		try {
			sem_timeline.acquire();
			
			processRecievedData();
			
			weiboPageAdapter.setData(profileImageUrlList, stringTimeline, middleImageUrlList);
			listview.setAdapter(weiboPageAdapter);

			setContentView(listview);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sem_timeline.release();
		}
		Log.d(TAG_PIC, "The UI layout was reset.\n");
    }


 
}
