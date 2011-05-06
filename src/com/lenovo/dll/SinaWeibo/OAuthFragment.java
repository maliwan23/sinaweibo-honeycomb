package com.lenovo.dll.SinaWeibo;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.RequestToken;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OAuthFragment extends Fragment {

    private View mView = null;
    private InitTask mInitTask;
	private RequestToken mRequestToken;
    private String mDirectUrl;
	private String mUserName;
	private String mPassword;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.login, container, false);
        System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);

        Button beginOAuthBtn = (Button) mView.findViewById(R.id.btnLogin);
        beginOAuthBtn.setOnClickListener(new Button.OnClickListener()
        {

            //@Override
            public void onClick( View v )
            {
                EditText txtUserName = (EditText) mView.findViewById(R.id.txtUserName);
                mUserName = txtUserName.getText().toString();
                if (mUserName.length() == 0)
                {
                	txtUserName.requestFocus();
                    Toast.makeText(v.getContext(), "Please input your user name!", Toast.LENGTH_LONG).show();
                    return;
                } 

                EditText txtPassword = (EditText) mView.findViewById(R.id.txtPassword);
                mPassword = txtPassword.getText().toString();
                if (mPassword.length() == 0)
                {
                	txtPassword.requestFocus();
                    Toast.makeText(v.getContext(), "Please input your password!", Toast.LENGTH_LONG).show();
                    return;
                } 
                
                mInitTask = new InitTask();
                mInitTask.execute();
            }
        } );
        
        Button btnSkipLogin = (Button) mView.findViewById(R.id.btnSkipLogin);
        btnSkipLogin.setOnClickListener(new Button.OnClickListener()
        {

			@Override
			public void onClick(View v) {
				if (OAuthConstant.getInstance().loadSettings(OAuthFragment.this.getActivity())) {
					setEnabled(false);
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("weibo4andriod://SkipLogin")));
	        	} else {
	                Toast.makeText(getActivity(), "Sorry, can't skip for the first time!", Toast.LENGTH_LONG).show();
	        	}
			}
        });

        return mView;
    }
    
    protected class InitTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground( Void... params ) 
        {
            Weibo weibo = OAuthConstant.getInstance().init();
            try {
                mRequestToken = weibo.getOAuthRequestToken("weibo4andriod://OAuthActivity");
                OAuthConstant.getInstance().setRequestToken(mRequestToken);
                return OAuthProcess();

            } catch (WeiboException e) {
                e.printStackTrace();
            }
            return false;
        }
        
        // -- gets called just before thread begins
        @Override
        protected void onPreExecute() 
        {
            setEnabled(false);
        }
        
        // -- called as soon as doInBackground method completes
        // -- notice that the third param gets passed to this method
        @Override
        protected void onPostExecute( Boolean success ) 
        {
        	if (success) {
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mDirectUrl)));
        	} else {
                Toast.makeText(getActivity(), "Failed to login!", Toast.LENGTH_LONG).show();
                setEnabled(true);
        	}
        }
    }
    
    private boolean OAuthProcess() {
		try {
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		
            nameValuePairs.add(new BasicNameValuePair("action", "submit"));
            nameValuePairs.add(new BasicNameValuePair("xtype", "null"));
            nameValuePairs.add(new BasicNameValuePair("from", "xweibo"));
            nameValuePairs.add(new BasicNameValuePair("display", "xweibo"));
            nameValuePairs.add(new BasicNameValuePair("oauth_callback", "weibo4andriod://OAuthActivity"));
            nameValuePairs.add(new BasicNameValuePair("oauth_token", mRequestToken.getToken()));
            nameValuePairs.add(new BasicNameValuePair("userId", mUserName));
            nameValuePairs.add(new BasicNameValuePair("passwd", mPassword));

    		HttpParams params = new BasicHttpParams();
    		params.setBooleanParameter("http.protocol.expect-continue", false);
    		
    		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    		HttpProtocolParams.setContentCharset(params, "utf-8");
    		
    		HttpPost post = new HttpPost("http://api.t.sina.com.cn/oauth/authorize");
    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

    		AndroidHttpClient client = AndroidHttpClient.newInstance("SinaWeibo");

    		mDirectUrl = client.execute(post, new ResponseHandler<String>() {
    			
				@Override
				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					
					int code = response.getStatusLine().getStatusCode();
					Log.e("handleResponse in OAuthProcess", String.format("%d", code));
					
					if (code == HttpStatus.SC_MOVED_TEMPORARILY) {
					    Header[] headers = response.getHeaders("Location");

					    if (headers != null && headers.length != 0) {
					        return headers[headers.length - 1].getValue();
					    }
					}
					return null;
				}});
    		
    		if (mDirectUrl != null && mDirectUrl.matches("weibo4andriod://OAuthActivity\\S+")) {
    			return true;
    		}
    		
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }
    
    public void setEnabled(boolean enabled) {
        View txtUserName = mView.findViewById(R.id.txtUserName);
        if (txtUserName != null) {
        	txtUserName.setEnabled(enabled);
        }

        View txtPassword = mView.findViewById(R.id.txtPassword);
        if (txtPassword != null) {
        	txtPassword.setEnabled(enabled);
        }

        View btnLogin = mView.findViewById(R.id.btnLogin);
        if (btnLogin != null) {
        	btnLogin.setEnabled(enabled);
        }
        
        View btnSkipLogin = mView.findViewById(R.id.btnSkipLogin);
        if (btnSkipLogin != null) {
        	btnSkipLogin.setEnabled(enabled);
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