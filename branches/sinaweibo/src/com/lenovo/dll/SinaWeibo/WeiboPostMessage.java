package com.lenovo.dll.SinaWeibo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.ImageItem;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class WeiboPostMessage extends Activity {
	
	private EditText text;
	private Button okay_button;
	private Button captured_button;
	private Status status;
	private static final String TAG = "weibo";
	private String inputContent;
	private Context mContext;
	private String UrlString;
	private Boolean captureclicked=false;
	
	private Semaphore sem_postmessage;
	private PostPrivateMessageThread postmsgthrd;
	
	private Weibo weibo;
	private ImageItem picture;
	private ImageView captured_image;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.postmessage);
		
		text = (EditText) findViewById(R.id.entry);
		
		okay_button = (Button)this.findViewById(R.id.okbutton);
		captured_button = (Button)this.findViewById(R.id.capturedbutton);
        postmsgthrd = new PostPrivateMessageThread();
        captured_image = new ImageView(this);
		captured_image = (ImageView)this.findViewById(R.id.capturedimage);
		captured_image.setVisibility(View.GONE);
        
		sem_postmessage = new Semaphore(1);
		
		captured_button.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				mContext = getApplicationContext();
				captureclicked = true;

				try {
				Drawable dw = Drawable.createFromPath("/sdcard/Pictures/1.jpg");
				captured_image.setImageDrawable(dw);
				captured_image.setVisibility(View.VISIBLE);
				//setContentView(R.layout.postmessage);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	
        okay_button.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v) {
				
				mContext = getApplicationContext();
				
				if (text.getText().length() == 0)
				{
					Toast.makeText(getApplicationContext(), "您还没有输入任何内容", Toast.LENGTH_SHORT).show();
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
				                Toast.makeText(getApplicationContext(), "发表微博成功！",
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

	}
	
	public void ImageGenerated() {
		try {
			
	        byte[] content= readFileImage("/sdcard/Pictures/1.jpg");
       
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
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}
}

