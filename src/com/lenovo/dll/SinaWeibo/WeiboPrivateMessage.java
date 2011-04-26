package com.lenovo.dll.SinaWeibo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class WeiboPrivateMessage extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView textview = new TextView(this);
        textview.setText("This is the Private Page tab");
        setContentView(textview);
        
    }

}


