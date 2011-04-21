package com.lenovo.microlog;

import java.util.ArrayList;
import android.os.Handler;
import android.os.Message;

/**
 * The broker is a kind of repository for {@link Handler} instances. It provides a simple and very convenient mechanism to 
 * establish communication between different handlers. Just subscribe a handler instance and use the returned address with 
 * this Broker. This class is thread safe, of course.
 * Usage example:
 * <code>
 * this.MyCompAddr = Broker.instance.subscribe( new Handler() );
 * // or, when using transceiver
 * this.MyRendererAddr = Broker.instance.subscribe( new MyTransceiver("Renderer").getHandler() );
 * [...]
 * // how to send messages
 * Message m = Message.obtain();
 * m.what = 1; // could be an identifier or op-code or whatever you want
 * Bundle b= new Bundle(); // or use more complex (key,value)-pairs.
 * b.putString("data", "Any Data");
 * m.setData(b);
 * Broker.instance.post(this.MyRendererAddr, m);
 * </code>
 *
 */
public class Broker {
        
        /**
         * The static instance.
         */
        public static Broker instance=new Broker(); 
        private ArrayList<Handler> mHandler;
        
        private Broker()
        {
                mHandler = new ArrayList<Handler> ();
        }

        
        /**
         * Subscribe a handler, so you can send messages easily to it.
         * Mind there is no check for doubled subscription.
         * @param h The handler
         * @return The "address" for the handler
         * @see {@link unsubscribe() }
         */
        public synchronized int subscribe(Handler h){
                ArrayList<Handler> hl=mHandler;
                hl.add(h);
                return hl.size()-1;
        }
        
        /**
         * Unsubscribes a handler. Invalid addresses are ignored 
         * @param address
         */
        public synchronized void unsubscribe(int address){
                if(isAddressValid(address))
                        mHandler.remove(address);
        }
        
        protected synchronized final boolean isAddressValid(int address){
                ArrayList<Handler> h=mHandler;
                return (!h.isEmpty() && address >= 0 && address < h.size() );
        }
        
        /**
         * Post a message to addressed receiver.
         * @param address
         * @param m
         * @return true, if address exists, i.e. message could be delivered
         */
        public synchronized boolean post(int address, Message m){
                ArrayList<Handler> h=mHandler;
                if(!isAddressValid(address))
                        return false;
                
                h.get(address).sendMessage(Message.obtain(m));
                return true;
                
        }
        
        /**
         * Sends a message to all connected handler
         * @param m The message
         * @return The number of notified handler, e.g. send messages.
         */
        public synchronized int broadcast(Message m)    {
                ArrayList<Handler> h=mHandler;
                int n=h.size();
                for(int i=0; i<n; ++i)
                {
                        h.get(i).sendMessage(Message.obtain(m));                
                }               
                return n;
        }
}
