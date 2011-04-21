package com.lenovo.microlog;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;

public class OAuthTask extends TimerTask implements Callback{
     public final static String TAG="OAuthTask";
     public final static int WHAT = 175; // any identifier 
     private Transceiver mTransceiver;
     private int mMyAddress=-1;
     private Timer mTimer;
     
     public OAuthTask(){
             mTransceiver = new Transceiver(TAG, this);
             mTimer = new Timer("SubsystemTimer");
             
     }

     public void onStart(){
             mMyAddress = Broker.instance.subscribe(mTransceiver.getHandler());
             // we use a timer to asynchronously post messages
             mTimer.schedule(this, 0, 125);
     }
     
     public void onQuit(){
             mTimer.cancel();
             Broker.instance.unsubscribe(mMyAddress);
     }
     
     public final int getBrokerAddress() { return mMyAddress; }
             
     @Override
     public boolean handleMessage(Message m) {
             switch(m.what){
             case OAuthTransceiver.WHAT:
                     Log.i(TAG,"Received message from " + OAuthTransceiver.TAG);
                     break;
             case MainActivity.WHAT:
                     Log.i(TAG,"Received message from " + MainActivity.TAG);
                     break;
             }
             // immediate response/delegate with a copy of our message
             Broker.instance.post(OAuthWrap.OAuthActivity, Message.obtain(m)); 
             return true;
     }

     @Override
     public void run() {
             Message m= new Message();
             m.what=WHAT;            
             Broker.instance.post(OAuthWrap.OAuthActivity, m);  
     }
}
