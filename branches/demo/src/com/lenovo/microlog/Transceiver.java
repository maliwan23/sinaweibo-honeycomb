package com.lenovo.microlog;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;

/**
 * The transceiver provides a convenient way to decorate an instance with receive and transmit capabilities.
 * It can (and this is encouraged) be used with the {@link Broker} instance.
 * The naming thing is not part of the Transeiver's functionality, but may be useful while debugging.
 * This class can be used for subclassing or as a "has-a"-relation, as it is possible to set
 * an arbitrary {@link Callback } listener.
 *
 */
public class Transceiver {
        private String mName="Transceiver"; 
        private static int ID=0;
        
        private HandlerThread mHandlerThread;
        private Handler mHandler;
        
        // delegates the messages, when no proprietary callback 
        // is associated with the transeiver
        private class HandlerListener implements Callback{ 
                
                @Override
                public boolean handleMessage(Message msg) {
                        return Transceiver.this.handleMessage(msg);
                }
                
        }
        
        /**
         * Creates a transceiver with an automatically generated name
         */
        public Transceiver(){
                mName = new String("Transceiver(" + ++ID + ")");
                create(new HandlerListener());
        }
        
        /**
         * Creates a transceiver with arbitrary name
         * @param name
         */
        public Transceiver(String name){
                ++ID;
                mName = name;
                create(new HandlerListener() );
        }
        
        /**
         * Creates a transceiver with arbitrary name and an own listener
         * @param name a custom name
         * @param l your own listener
         */
        public Transceiver(String name, Callback l){
                ++ID;
                mName = name;
                create(l);
        }
        
        
        private void create(Callback l){
                mHandlerThread = new HandlerThread(getName());
                mHandlerThread.start();
                Log.d(mName, "Waiting for looper...");
                // we need to wait (blocking) to get the looper instance
                while(!mHandlerThread.isAlive()) {};  
                mHandler = new Handler(mHandlerThread.getLooper(), l);
                Log.d(mName, "Ready");
        }
        
        /**
         * @return The name of this transceiver
         */
        public final String getName() { return mName; }
        
        /** 
         * @return Its internal handler
         */
        public final Handler getHandler() { return mHandler; }

        /**
         * The callback method for received messages. You can override it, when deriving from Transceiver.
         * @param msg the message
         * @return true, if the message was handled.
         */
        public boolean handleMessage(Message msg) { return false; }
}
