package com.lenovo.dll.SinaWeibo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class WeiboAtPage extends Fragment {
	
	
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
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.list_fragment, container, false);
    	listview = (ListView) view.findViewById(R.id.lstView);
    	hide();
    	return view;
    }
        
    public void update() {
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

		try {
			sem_mentions.acquire();
			
			getListData();

			weiboPageAdapter = new WeiboPageAdapter(this.getActivity(), profileImageUrlList, stringMentions, middleImageUrlList);
			
			listview.setAdapter(weiboPageAdapter);
			
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
			stringBuilder.append("<font color='#CC0000'>" + status.getUser().getScreenName() + ": </font>"
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
			} catch (Exception e){
				e.printStackTrace();
			} finally {
				sem_mentions.release();
			}
		}
	}

    public void show()
    {
    	try {
    	    FragmentTransaction ft = getFragmentManager().beginTransaction();
    	    ft.show(this);
    	    ft.commit();
    	    if (listview.getChildCount() == 0)
    	    	update();
    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
    }
    
    public void hide()
    {
    	try {
    	    FragmentTransaction ft = getFragmentManager().beginTransaction();
    	    ft.hide(this);
    	    ft.commit();
    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
    }
}
