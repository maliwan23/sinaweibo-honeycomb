package com.lenovo.dll.SinaWeibo;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoFragment extends Fragment {

    private VideoView viewer = null;
    MediaMetadataRetriever mmr = null;
    Bitmap bmp = null;
    public static String videoPath = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewer = (VideoView) inflater.inflate(R.layout.video_view, container, false);
        startVideo(getResources().getString(R.string.video_path));
        viewer.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
        return viewer;
    }

    public void startVideo(String path) {
        if (viewer == null)
            return;
        
        try {
            File file = new File(path);
            if (!file.exists()) {
                Toast.makeText(viewer.getContext(), "Video not exist!", Toast.LENGTH_LONG).show();
                return;
            }
            
            MediaController mediaController = new MediaController(this.getActivity());
            mediaController.setAnchorView(viewer);
            
            Uri video = Uri.parse(path);
            viewer.setVideoURI(video);
            
            viewer.setMediaController(mediaController);
//          viewer.setDrawingCacheEnabled(true);
//          viewer.setDrawingCacheBackgroundColor(0);
            viewer.requestFocus();

            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);
            
            viewer.start();
            videoPath = "video://" + path;
        } catch (Exception ex) {
            Log.e(this.toString(), ex.toString());
        }
    }

    public void resumeVideo() {
        if (viewer == null)
            return;
        
        try {
            viewer.start();
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
            
            Thread th = new Thread() {
                public void run() {
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream("/sdcard/snapshot.jpg");
                        bmp.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            th.start();
        } catch (Exception ex) {
            Log.e(this.toString(), ex.toString());
        }
    }
    
    public Bitmap getSnapshot()
    {
        return bmp;
    }

}
