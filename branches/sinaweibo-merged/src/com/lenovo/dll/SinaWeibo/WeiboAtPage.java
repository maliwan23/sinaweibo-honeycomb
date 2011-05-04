package com.lenovo.dll.SinaWeibo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class WeiboAtPage extends Fragment {
    
    
    private ListView listview;
    private WeiboPageAdapter weiboPageAdapter;

    
    private List<Status> mentions;
    private List<String> stringMentions;
    private List<String> profileImageUrlList;
    private List<String> middleImageUrlList;
    
    private Weibo weibo;
    private static final String TAG="Weibo";
    
    private long latestUpdate = 0;
    private long previousUpdate = 0;

    Timer mTimer;
    TimerTask mTimerTask;
    UpdateTask mUpdateTask = new UpdateTask();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        listview = (ListView) view.findViewById(R.id.lstView);
        hide();
        return view;
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
        
        if (mentions.size() > 0)
        {
            latestUpdate = mentions.get(0).getCreatedAt().getTime();
        }
    }
    
    public void show()
    {
        try {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.show(this);
            ft.commit();
            
            if (mTimer == null) {
                mTimer = new Timer(true);
                mTimerTask = new TimerTask()
                {
                    public void run()
                    {
                        updateAsync();
                    }        
                };
                mTimer.schedule(mTimerTask, 100, 60000);
            }

//          if (listview.getChildCount() == 0)
//              update();
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

    class UpdateTask extends AsyncTask<Void, Void, Boolean> {
        
        private boolean isRunning = false;

        public boolean isRunning() {
            return isRunning;
        }

        @Override
        protected void onPreExecute() {
            isRunning = true;
        }
        
        @Override
        protected Boolean doInBackground(Void... params) {
            
            try {
                stringMentions = new ArrayList<String>();
                profileImageUrlList = new ArrayList<String>();
                middleImageUrlList = new ArrayList<String>();

                weibo = OAuthConstant.getInstance().getWeibo();
                weibo.setToken(OAuthConstant.getInstance().getToken(), OAuthConstant.getInstance().getTokenSecret());
                
                mentions = weibo.getMentions();

                getListData();
                    
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        
        @Override
        protected void onPostExecute(final Boolean success) {
            if (success)
                updateUIList();
            mUpdateTask = new UpdateTask();
            isRunning = false;
        }
    }
    
    private void updateAsync()
    {
        try {
            if (!mUpdateTask.isRunning())
                mUpdateTask.execute();
        } catch (Exception e) {
            Log.e("updateAsync", e.toString());
        }
    }
    
    private void updateUIList()
    {
        try {
            if (latestUpdate > previousUpdate) {
                weiboPageAdapter = new WeiboPageAdapter(this.getActivity(), profileImageUrlList, stringMentions, middleImageUrlList);
                listview.setAdapter(weiboPageAdapter);

                previousUpdate = latestUpdate;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("weibo4andriod://NewMessage"));
                startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
