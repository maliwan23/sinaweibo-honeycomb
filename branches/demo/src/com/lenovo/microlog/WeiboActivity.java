package com.lenovo.microlog;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class WeiboActivity extends Activity implements OnTouchListener, OnGestureListener {

	private GestureDetector mGestureDetector;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	setContentView(R.layout.weibo_fragment);
        	mGestureDetector = new GestureDetector(this);
        	
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
        	
        	Button btnExit = (Button) findViewById(R.id.btnExit);
        	btnExit.setOnClickListener( new Button.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                	finish();
                }
            } );
        	
        	Button btnWeibo = (Button) findViewById(R.id.btnWeibo);
        	btnWeibo.setOnClickListener( new Button.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                	try {
        	    	    FloatFragment ff = (FloatFragment) getFragmentManager().findFragmentById(R.id.float_fragment);
        	    	    WeiboFragment wf = (WeiboFragment) getFragmentManager().findFragmentById(R.id.weibo_fragment);
        	    	    ff.hide();
        	    	    wf.show();
            		} catch (Exception ex) {
                		Log.e(this.toString(), ex.toString());
                	}
                }
            } );
        	
        	try {
	    	    WeiboFragment f = (WeiboFragment) getFragmentManager().findFragmentById(R.id.weibo_fragment);
	    	    f.hide();
        	} catch (Exception ex) {
        		Log.e(this.toString(), ex.toString());
        	}
        	
        	View touchView = (View) findViewById(R.id.imgSnapshot);
        	touchView = (View) touchView.getParent();
        	touchView.setLongClickable(true);
        	touchView.setOnTouchListener(this);

        } catch (Exception ex) {
        	Log.e(this.toString(), ex.toString());
        }
    }
    
    private void startVideo() {
        Intent showContent = new Intent(getApplicationContext(),
                VideoActivity.class);
        startActivity(showContent);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        if (keyCode == KeyEvent.KEYCODE_S) {
        	try {
	    	    FloatFragment ff = (FloatFragment) getFragmentManager().findFragmentById(R.id.float_fragment);
	    	    WeiboFragment wf = (WeiboFragment) getFragmentManager().findFragmentById(R.id.weibo_fragment);
	    	    if (wf.isHidden()) {
	    	    	ff.hide();
	    	    	wf.show();
	    	    } else {
	        	    wf.hide();
	        	    ff.show();
	    	    }
        	} catch (Exception ex) {
        		Log.e(this.toString(), ex.toString());
        	}
            return true;
        }
        return super.onKeyDown(keyCode, msg);
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Toast.makeText(this, event.toString(), Toast.LENGTH_LONG).show();
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e2.getX() - e1.getX() > 50 && velocityX > 100) {
        	try {
	    	    FloatFragment ff = (FloatFragment) getFragmentManager().findFragmentById(R.id.float_fragment);
	    	    WeiboFragment wf = (WeiboFragment) getFragmentManager().findFragmentById(R.id.weibo_fragment);
	    	    if (!wf.isHidden()) {
	        	    wf.hide();
	        	    ff.show();
	    	    }
        	} catch (Exception ex) {
        		Log.e(this.toString(), ex.toString());
        	}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
