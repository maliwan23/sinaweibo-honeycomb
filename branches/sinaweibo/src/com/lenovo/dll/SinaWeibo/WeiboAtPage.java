package com.lenovo.dll.SinaWeibo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class WeiboAtPage extends Activity{
	
	
	private ListView listview;
	private WeiboPageAdapter weiboPageAdapter;
	
	private GetMentionsThread getMentionsThread;
	
	private List<Status> mentions;
	private List<String> stringMentions;
	private List<String> profileImageUrlList;
	private List<String> middleImageUrlList;
	
	private Semaphore sem_mentions;
	private Weibo weibo;
	private static final String TAG="Weibo";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		stringMentions = new ArrayList<String>();
		profileImageUrlList = new ArrayList<String>();
		middleImageUrlList = new ArrayList<String>();

        getMentionsThread = new GetMentionsThread();
        
        weibo = OAuthConstant.getInstance().getWeibo();
        weibo.setToken(OAuthConstant.getInstance().getToken(), OAuthConstant.getInstance().getTokenSecret());
        
		sem_mentions = new Semaphore(1);
        
		try {
			sem_mentions.acquire();
			getMentionsThread.start();	
		} catch (Exception e) {
			e.printStackTrace();
		}

	
		listview = new ListView(this);
		
		try {
			sem_mentions.acquire();
			
			getListData();

			weiboPageAdapter=new WeiboPageAdapter(this);
			
			weiboPageAdapter.setData(profileImageUrlList, stringMentions, middleImageUrlList);
			
			listview.setAdapter(weiboPageAdapter);

			setContentView(listview);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sem_mentions.release();
		}

    }
    
	
	private void getListData(){

		StringBuilder stringBuilder = new StringBuilder("1");
		
		for (Status status : mentions) {
			stringBuilder.setLength(0);
			stringBuilder.append(status.getUser().getScreenName() + "说:"
					+ status.getText() + "\n");
			stringMentions.add(stringBuilder.toString());
			Log.d(TAG, stringBuilder.toString());
			
			profileImageUrlList.add(status.getUser().getProfileImageURL().toString());
			if (status.getBmiddle_pic() != null && !status.getBmiddle_pic().toString().equals(""))
			{
				middleImageUrlList.add(status.getBmiddle_pic().toString());
			}else {
				middleImageUrlList.add("NoPhotos");
			}
			
			Log.d(TAG, status.getUser().getProfileImageURL().toString());
		}
		
	}
    
	class GetMentionsThread extends Thread {

		public void run(){
			try {
			mentions = weibo.getMentions();
						
			} catch (WeiboException e){
				e.printStackTrace();
			} finally {
				sem_mentions.release();
			}
		}
	}

}
