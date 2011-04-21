package com.lenovo.microlog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CommentActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_view);
        
    	try {
            Intent launchingIntent = getIntent();
            Uri bmp = launchingIntent.getData();

            ImageView img = (ImageView) findViewById(R.id.imgSnapshot);
        	img.setImageURI(bmp);
        
        	Button btnOK = (Button) findViewById(R.id.btnOK);
        	btnOK.setOnClickListener( new Button.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                	finish();
                }
            } );

    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
    }

}
