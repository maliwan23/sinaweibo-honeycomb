package com.lenovo.dll.SinaWeibo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {

    private View viewer = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewer = inflater.inflate(R.layout.main_view, container, false);
        
    	try {
//            Resources res = getResources(); // Resource object to get Drawables
//            TabHost tabHost = ((TabActivity) this.getActivity()).getTabHost();  // The activity TabHost
//            TabHost.TabSpec spec;  // Resusable TabSpec for each tab
//            Intent intent;  // Reusable Intent for each tab
//            
//            // Create an Intent to launch an Activity for the tab (to be reused)
//            intent = new Intent().setClass(this.getActivity(), WeiboHomePage.class);
//
//            // Initialize a TabSpec for each tab and add it to the TabHost
//            spec = tabHost.newTabSpec("homePage").setIndicator("首页",
//                              res.getDrawable(R.drawable.homepage_icon))
//                          .setContent(intent);
//            tabHost.addTab(spec);
//
//            // Do the same for the other tabs
//            intent = new Intent().setClass(this.getActivity(), WeiboAtPage.class);
//            spec = tabHost.newTabSpec("atPage").setIndicator("@我的",
//                              res.getDrawable(R.drawable.atpage_icon))
//                          .setContent(intent);
//            tabHost.addTab(spec);
//
//            intent = new Intent().setClass(this.getActivity(), WeiboPostMessage.class);
//            spec = tabHost.newTabSpec("postMessage").setIndicator("撰写",
//                              res.getDrawable(R.drawable.postmessage_icon))
//                          .setContent(intent);
//            tabHost.addTab(spec);
//
//            tabHost.setCurrentTab(0);
            
//            tabHost.getTabContentView().setLayoutParams(new LinearLayout.LayoutParams(200,200));
//            tabHost.setLayoutParams(new LinearLayout.LayoutParams(200,200));

//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//    	    ft.hide(this);
//    	    ft.commit();
    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
        return viewer;
    }

    public void show()
    {
    	try {
    	    FragmentTransaction ft = getFragmentManager().beginTransaction();
//    	    ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_in);
    	    ft.show(this);
    	    ft.commit();
    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
    }

    public void hide()
    {
    	try {
    	    FragmentTransaction ft = getFragmentManager().beginTransaction();
//    	    ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
    	    ft.hide(this);
    	    ft.commit();
    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
    }

}
