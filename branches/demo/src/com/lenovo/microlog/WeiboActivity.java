package com.lenovo.microlog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class WeiboActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	setContentView(R.layout.weibo_fragment);
//        	startVideo();
        	
        	Button btnStartVideo = (Button) findViewById(R.id.btnStartVideo);
        	btnStartVideo.setOnClickListener( new Button.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                	try {
                	    VideoFragment video = (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment);

		                if (video == null || !video.isInLayout()) {
		                    Intent showContent = new Intent(getApplicationContext(), VideoActivity.class);
		                    startActivity(showContent);
		                } else {
		                    video.startVideo();
		                }
                	} catch (Exception ex) {
                		Log.e(this.toString(), ex.toString());
                	}
                }
            } );
        	
        	Button btnStepVideo = (Button) findViewById(R.id.btnStepVideo);
        	btnStepVideo.setOnClickListener( new Button.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                	try {
                	    VideoFragment video = (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment);

		                if (video == null || !video.isInLayout()) {
		                    Intent showContent = new Intent(getApplicationContext(), VideoActivity.class);
		                    startActivity(showContent);
		                } else {
		                    video.stepVideo();
		                }
                	} catch (Exception ex) {
                		Log.e(this.toString(), ex.toString());
                	}
                }
            } );
        	
        	Button btnPauseVideo = (Button) findViewById(R.id.btnPauseVideo);
        	btnPauseVideo.setOnClickListener( new Button.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                	try {
                	    VideoFragment video = (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment);

		                if (video == null || !video.isInLayout()) {
		                    Intent showContent = new Intent(getApplicationContext(), VideoActivity.class);
		                    startActivity(showContent);
		                } else {
		                    video.pauseVideo();
		                }
                	} catch (Exception ex) {
                		Log.e(this.toString(), ex.toString());
                	}
                }
            } );
        	
        	Button btnResumeVideo = (Button) findViewById(R.id.btnResumeVideo);
        	btnResumeVideo.setOnClickListener( new Button.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                	try {
                	    VideoFragment video = (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment);

		                if (video == null || !video.isInLayout()) {
		                    Intent showContent = new Intent(getApplicationContext(), VideoActivity.class);
		                    startActivity(showContent);
		                } else {
		                    video.resumeVideo();
		                }
                	} catch (Exception ex) {
                		Log.e(this.toString(), ex.toString());
                	}
                }
            } );
        	
        	Button btnCaptureVideo = (Button) findViewById(R.id.btnCaptureVideo);
        	btnCaptureVideo.setOnClickListener( new Button.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                	try {
                	    VideoFragment video = (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment);

		                if (video == null || !video.isInLayout()) {
		                    Intent showContent = new Intent(getApplicationContext(), VideoActivity.class);
		                    startActivity(showContent);
		                } else {
		                    video.captureVideo();
		                    
//		                    Intent comment = new Intent(getApplicationContext(), CommentActivity.class);
//		                    comment.setData(Uri.parse("/sdcard/snapshot.bmp"));
//		                    startActivity(comment);
		                    
		                    ImageView img = (ImageView) findViewById(R.id.imgSnapshot);
		                    img.setImageBitmap(video.getSnapshot());
//		                	img.setImageURI(Uri.parse("/sdcard/snapshot.png"));
		                }
                	} catch (Exception ex) {
                		Log.e(this.toString(), ex.toString());
                	}
                }
            } );
        	
        } catch (Exception ex) {
        	Log.e(this.toString(), ex.toString());
        }
    }
    
    private void startVideo() {
        Intent showContent = new Intent(getApplicationContext(),
                VideoActivity.class);
        startActivity(showContent);
    }

}
