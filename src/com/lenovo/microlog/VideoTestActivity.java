package com.lenovo.microlog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoTestActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_test);
		
    	Button btnPlay = (Button) findViewById(R.id.btnPlay);
    	btnPlay.setOnClickListener( new Button.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
            	play();
            }
        } );
	}

    public void play()
    {
    	try {
	        VideoView view = (VideoView) findViewById(R.id.video_test);
	        
			MediaController mediaController = new MediaController(this.getApplicationContext());
			mediaController.setAnchorView(view);
			
			Uri video = Uri.parse("/sdcard/transformers.dark.of.the.moon.mp4");
			view.setMediaController(mediaController);
			view.setVideoURI(video);
			view.setDrawingCacheEnabled(true);
			view.start();
    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
    }

}
