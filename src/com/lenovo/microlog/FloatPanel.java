package com.lenovo.microlog;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class FloatPanel {
	
	private View mView;
	private LayoutParams mParams;
	
	public FloatPanel(Context context)
	{
		try {
	    	mView = View.inflate(context, R.layout.floatpanel, null);
	    	mView.setOnFocusChangeListener(mFocusChangeListener);
	    	mView.setOnClickListener(mClickListener);
	    	
	        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	
	        mParams = new LayoutParams();
	        mParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL;
	        mParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
	        mParams.type = LayoutParams.TYPE_PHONE;
	        mParams.format = PixelFormat.RGBA_8888;
	        mParams.width = 300;
	        mParams.height = 600;
	        mParams.alpha = 100;
	        
	        wm.addView(mView, mParams);
	        
	    	Button btnShare = (Button) mView.findViewById(R.id.btnShare);
	    	btnShare.setOnClickListener( new Button.OnClickListener()
	        {
	            @Override
	            public void onClick( View v )
	            {
	            	try {
	                    Intent fragment = new Intent(mView.getContext(),
	                    		MainActivity.class);
	                    fragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                    mView.getContext().startActivity(fragment);
	            	} catch (Exception ex) {
	            		Log.e(this.toString(), ex.toString());
	            	}
	            }
	        } );

		} catch (Exception ex) {
			Log.e(this.toString(), ex.toString());
		}
	}
	
	public void close(Context context)
	{
		try {
	        WindowManager wm = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
	        wm.removeView(mView);
		} catch (Exception ex) {
			Log.e(this.toString(), ex.toString());
		}
	}
	
	public void updateView(Context context, boolean hasFocus)
	{
		try {
			if (hasFocus) {
				mParams.type = LayoutParams.TYPE_PHONE;
			} else {
				mParams.type = LayoutParams.TYPE_TOAST;
			}
	        WindowManager wm = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
	        wm.updateViewLayout(mView, mParams);
		} catch (Exception ex) {
			Log.e(this.toString(), ex.toString());
		}
	}
	
	private OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener()
	{
		public void onFocusChange(View view, boolean hasWindowFocus)
		{
//			updateView(view.getContext(), hasWindowFocus);
		}
	};
	
	private OnClickListener mClickListener = new OnClickListener()
	{
		public void onClick(View view)
		{
//			updateView(view.getContext(), mParams.type == LayoutParams.TYPE_TOAST);
		}
	};
}
