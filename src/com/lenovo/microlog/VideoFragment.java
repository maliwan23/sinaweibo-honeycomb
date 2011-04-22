package com.lenovo.microlog;

import java.io.FileOutputStream;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoFragment extends Fragment {

    private VideoView viewer = null;
	MediaMetadataRetriever mmr = null;
	Bitmap bmp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewer = (VideoView) inflater.inflate(R.layout.video_view, container, false);
        startVideo();
        return viewer;
    }

    public void startVideo() {
    	if (viewer == null)
    		return;
    	
	    try {
			MediaController mediaController = new MediaController(this.getActivity());
			mediaController.setAnchorView(viewer);
			
			String path = getResources().getString(R.string.video_path);
			Uri video = Uri.parse(path);
			viewer.setVideoURI(video);
			
			viewer.setMediaController(mediaController);
//			viewer.setDrawingCacheEnabled(true);
//			viewer.setDrawingCacheBackgroundColor(0);
			viewer.requestFocus();
//			viewer.start();

			mmr = new MediaMetadataRetriever();
			mmr.setDataSource(path);
	    } catch (Exception ex) {
	    	Log.e(this.toString(), ex.toString());
	    }
    }

    public void stepVideo() {
    	if (viewer == null)
    		return;
    	
	    try {
	    	viewer.resume();
	    	viewer.pause();
	    } catch (Exception ex) {
	    	Log.e(this.toString(), ex.toString());
	    }
    }

    public void pauseVideo() {
    	if (viewer == null)
    		return;
    	
	    try {
	    	viewer.pause();
	    } catch (Exception ex) {
	    	Log.e(this.toString(), ex.toString());
	    }
    }

    public void resumeVideo() {
    	if (viewer == null)
    		return;
    	
	    try {
	    	viewer.resume();
	    } catch (Exception ex) {
	    	Log.e(this.toString(), ex.toString());
	    }
    }
    
    public void captureVideo() {
    	if (viewer == null)
    		return;
    	
	    try {
	    	if (mmr == null)
	    	{
				mmr = new MediaMetadataRetriever();
				mmr.setDataSource(getResources().getString(R.string.video_path));
	    	}
	    	
			int pos = viewer.getCurrentPosition();
			bmp = mmr.getFrameAtTime(pos * 1000);
//			FileOutputStream out = new FileOutputStream("/sdcard/snapshot.png");
//			bmp.compress(Bitmap.CompressFormat.PNG, 50, out);
	    } catch (Exception ex) {
	    	Log.e(this.toString(), ex.toString());
	    }
    }
    
    public Bitmap getSnapshot()
    {
    	return bmp;
    }

}
