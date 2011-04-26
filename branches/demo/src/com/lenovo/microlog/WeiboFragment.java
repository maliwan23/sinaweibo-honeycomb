package com.lenovo.microlog;

import java.util.regex.Pattern;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WeiboFragment extends Fragment {

    private View viewer = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewer = inflater.inflate(R.layout.weibo_view, container, false);
        
    	try {
    	    FragmentTransaction ft = getFragmentManager().beginTransaction();
    	    ft.hide(this);
    	    ft.commit();

            Pattern pattern = Pattern.compile("video://\\S+");
            String scheme = "video://";

            TextView comment = (TextView) viewer.findViewById(R.id.txtComment);
            comment.setText("Test video link: \n\nvideo:// \n\nvideo:///sdcard/Tom.Clancy_s_.H.A.W.X.2.Official.Trailer.mp4 \n\nvideo://a \n\nvideo://b.mp4 \n\nvideo:///sdcard/transformers.dark.of.the.moon.mp4\n\nvideo:///sdcard/H264_BP_720p_8Mbps_24fps_aac_58mins_L4.3gp.mp4 \n\nvideo:///sdcard/机器人总动员番外篇：电焊工.mp4\n\n");
            Linkify.addLinks(comment, pattern, scheme);
            
    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
        return viewer;
    }

    public void show()
    {
    	try {
    	    FragmentTransaction ft = getFragmentManager().beginTransaction();
    	    ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_in);
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
    	    ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
    	    ft.hide(this);
    	    ft.commit();
    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
    }

}
