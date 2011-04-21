package com.lenovo.microlog;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        try {
			setContentView(R.layout.video_fragment);

//	        VideoFragment viewer = (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment);
//	        viewer.startVideo();
        } catch (Exception ex) {
        	Log.e(this.toString(), ex.toString());
        }
	}
}