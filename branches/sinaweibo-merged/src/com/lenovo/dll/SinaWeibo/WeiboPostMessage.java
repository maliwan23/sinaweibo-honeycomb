package com.lenovo.dll.SinaWeibo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.ImageItem;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WeiboPostMessage extends Fragment {
	
	private EditText text;
	private Button button;
	private Status status;
	private static final String TAG = "weibo";
	private String UrlString;
	private String inputContent;
	
	private Semaphore sem_postmessage;
	private PostPrivateMessageThread postmsgthrd;
	
	private Weibo weibo;
	private ImageItem picture;
	private View view = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	view = inflater.inflate(R.layout.postmessage, container, false);
    	hide();
		
		text = (EditText) view.findViewById(R.id.entry);
		
		button = (Button) view.findViewById(R.id.ok);
		
        postmsgthrd = new PostPrivateMessageThread();
        
		sem_postmessage = new Semaphore(1);
		
		
        button.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v) {
				
				view.getContext();
				
				if (text.getText().length() == 0)
				{
					Toast.makeText(view.getContext(), "您还没有输入任何内容", Toast.LENGTH_SHORT).show();
				} 
				else 
				{
				    try {

				    	    inputContent = text.getText().toString();
				    	    
				          	weibo = OAuthConstant.getInstance().getWeibo();

				          	try {
								sem_postmessage.acquire();
								
								ImageGenerated();
								
								postmsgthrd.start();
				          	} catch (Exception e) {
								e.printStackTrace();
							} 
				   			
				   			sem_postmessage.acquire();
				   			
				   			
				          	Log.d(TAG, status.getId() + " : "+ status.getText()+"  "+status.getCreatedAt());
				          	
				          	if ( status.getText() != null && !status.getText().equals("") )
				          	{
				                // When clicked, show a toast with the TextView text
				                Toast.makeText(view.getContext(), "发表微博成功",
				                    Toast.LENGTH_SHORT).show();
				                
				                //Intent i = new Intent(mContext, WeiboHomePage.class);
				                //startActivity(i); 
				          	}

				   		} catch (Exception e) {
				   			e.printStackTrace();
				   		} finally {
							sem_postmessage.release();
						}

				}
				
			}        	
        });
        return view;
	}

	public void ImageGenerated() {
		try {
	        byte[] content= readFileImage("/sdcard/snapshot.png");
	        
			if(content.length <= 0){
				System.out.println("Image file is null");
			}
	        
	        picture=new ImageItem("pic",content);
	        UrlString =java.net.URLEncoder.encode(inputContent,"UTF-8");
		} catch (IOException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class PostPrivateMessageThread extends Thread {
	
		public void run(){
			try {
	          	status = weibo.uploadStatus(UrlString, picture);		
			} catch (WeiboException e){
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}finally {
				sem_postmessage.release();
			}

		}
	}

	public static byte[] readFileImage(String filename)throws IOException {
		BufferedInputStream bufferedInputStream=new BufferedInputStream(
				new FileInputStream(filename));
		int len =bufferedInputStream.available();
		byte[] bytes=new byte[len];
		int r=bufferedInputStream.read(bytes);
		if(len !=r){
			bytes=null;
			throw new IOException("读�?�文件�?正确");
		}
		bufferedInputStream.close();
		return bytes;
	}

    public void show()
    {
    	try {
    	    FragmentTransaction ft = getFragmentManager().beginTransaction();
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
    	    ft.hide(this);
    	    ft.commit();
    	} catch (Exception ex) {
    		Log.e(this.toString(), ex.toString());
    	}
    }
}

