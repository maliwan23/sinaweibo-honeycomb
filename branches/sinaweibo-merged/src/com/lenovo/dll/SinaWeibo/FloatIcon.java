package com.lenovo.dll.SinaWeibo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class FloatIcon extends Fragment {
	
    private View mView = null;
    private boolean mAlert = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	mView = inflater.inflate(R.layout.float_view, container, false);
        return mView;
    }

    public void show()
    {
    	try {
    	    FragmentTransaction ft = getFragmentManager().beginTransaction();
    	    ft.setCustomAnimations(R.anim.bounce_in, R.anim.bounce_out);
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
    	    ft.setCustomAnimations(R.anim.bounce_in, R.anim.bounce_out);
    	    ft.hide(this);
    	    ft.commit();
    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
    }
    
    private Thread th = null;
    
    public void alert(boolean bAlert)
    {
    	mAlert = bAlert;
    	if (!bAlert || mView == null)
    		return;

//		final Animation anim_out = new AlphaAnimation(1.0f, 0.0f);
//		anim_out.setDuration(1500);
		
		final Animation anim_in = new AlphaAnimation(0.0f, 1.0f);
		anim_in.setDuration(1500);

		if (th != null) {
			th.stop();
		}
		
		th = new Thread() {
			public void run()
			{
				while (mAlert)
				{
			    	try {
//			    		mView.startAnimation(anim_out);
//			    		Thread.sleep(1500);
			    		mView.startAnimation(anim_in);
			    		Thread.sleep(1500);
			    	} catch (Exception ex) {
			    		Log.e(this.toString(), ex.toString());
			    	}
				}
				mView.setAlpha(1.0f);
			}
		};
		th.start();
    }
}
