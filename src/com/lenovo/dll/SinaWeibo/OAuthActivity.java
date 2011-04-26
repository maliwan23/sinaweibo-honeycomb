package com.lenovo.dll.SinaWeibo;

import java.util.concurrent.Semaphore;

import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OAuthActivity extends Activity {
	
	private Context mContext;
	private RequestToken requestToken;
	private AccessToken accessToken;
	private HttpRequestThread httpRequestThread;
	private Uri uri;
	private Semaphore sem_http;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		httpRequestThread = new HttpRequestThread();
		
		sem_http = new Semaphore(1);
		uri = this.getIntent().getData();
			
		try {
			sem_http.acquire();
			httpRequestThread.start();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		try {
			sem_http.acquire();
			TextView tv = (TextView) findViewById(R.id.TextView01);
			
			tv.setText("得到AccessToken的key和Secret,可以使用这两个参数进行授权登录了.\n Access token:\n"+accessToken.getToken()+"\n Access token secret:\n"+accessToken.getTokenSecret());
			Button button =  (Button) findViewById(R.id.Button_Load);
			button.setText("现在可以登陆了");
			button.setOnClickListener(new Button.OnClickListener()
	        {
	            //@Override
	            public void onClick(View v)
	            {

						mContext = getApplicationContext();
	            		Intent intent = new Intent(mContext, WeiboTabWidget.class);
	            		startActivity(intent);
	            }
	        } );
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			sem_http.release();	
		}
	}
	

	class HttpRequestThread extends Thread {
		public void run(){
	
			try {
				requestToken= OAuthConstant.getInstance().getRequestToken();
				accessToken=requestToken.getAccessToken(uri.getQueryParameter("oauth_verifier"));
				OAuthConstant.getInstance().setAccessToken(accessToken);		
			} catch (WeiboException e) {
				e.printStackTrace();
			
			} finally {
				sem_http.release();
			}
		}
	}
	

}
