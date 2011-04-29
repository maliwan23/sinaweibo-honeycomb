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
    	    alert(false);
    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
    }
    
    private Thread th = null;
    
    public void alert(boolean bAlert)
    {
    	mAlert = (bAlert && !this.isHidden() && mView != null);
    	if (!mAlert)
    		return;

		if (th == null) { 
			th = new Thread() {
				public void run()
				{
					final Animation anim_in = new AlphaAnimation(0.0f, 1.0f);
					anim_in.setDuration(1000);
					final Animation anim_out = new AlphaAnimation(1.0f, 0.0f);
					anim_out.setDuration(1000);
					
					Runnable action_in = new Runnable() {
					    public void run() {
				    		mView.startAnimation(anim_in);
					    }
					};

					Runnable action_out = new Runnable() {
					    public void run() {
				    		mView.startAnimation(anim_out);
					    }
					};

					while (mAlert)
					{
				    	try {
				    		FloatIcon.this.getActivity().runOnUiThread(action_out);
				    		Thread.sleep(1000);
				    		FloatIcon.this.getActivity().runOnUiThread(action_in);
				    		Thread.sleep(1100);
				    	} catch (Exception ex) {
				    		Log.e(this.toString(), ex.toString());
				    	}
					}
					th = null;
				}
			};
			th.start();
		}
    }
}
