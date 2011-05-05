package com.lenovo.dll.SinaWeibo;

import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.RequestToken;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class OAuthFragment extends Fragment {


    private View mView = null;
    private Uri mUri;
    private InitTask mInitTask;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.login, container, false);
        System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);

        Button beginOuathBtn = (Button) mView.findViewById(R.id.Button_Load);
        beginOuathBtn.setOnClickListener(new Button.OnClickListener()
        {

            //@Override
            public void onClick( View v )
            {
                v.setEnabled(false);
                mInitTask = new InitTask();
                mInitTask.execute(v);
                
            }
        } );

        return mView;
    }
    
    protected class InitTask extends AsyncTask<View, Boolean, View>
    {
        @Override
        protected View doInBackground( View... params ) 
        {
            Weibo weibo = OAuthConstant.getInstance().init();
            RequestToken requestToken;
            try {
                requestToken = weibo.getOAuthRequestToken("weibo4andriod://OAuthActivity");
                mUri = Uri.parse(requestToken.getAuthenticationURL()+ "&from=xweibo");
                OAuthConstant.getInstance().setRequestToken(requestToken);
                
            } catch (WeiboException e) {
                e.printStackTrace();
                Toast.makeText(OAuthFragment.this.getActivity(), "Failed to login!", Toast.LENGTH_LONG);
            
            }
            return params[0];
        }
        
        // -- gets called just before thread begins
        @Override
        protected void onPreExecute() 
        {
                super.onPreExecute();
                
        }
        
        // -- called from the publish progress 
        // -- notice that the datatype of the second param gets passed to this method
        @Override
        protected void onProgressUpdate(Boolean... values) 
        {
                super.onProgressUpdate(values);

        }
        
        // -- called if the cancel button is pressed
        @Override
        protected void onCancelled()
        {
                super.onCancelled();

        }
 
        // -- called as soon as doInBackground method completes
        // -- notice that the third param gets passed to this method
        @Override
        protected void onPostExecute( View v ) 
        {
                try {
                	v.setVisibility(View.GONE); 
                    WebView wvLogin = (WebView) OAuthFragment.this.getView().findViewById(R.id.webViewLogin);
                    if (wvLogin != null) {
                        WebSettings settings = wvLogin.getSettings();
                        settings.setBuiltInZoomControls(true);
                        settings.setDisplayZoomControls(true);
                        settings.setDefaultZoom(ZoomDensity.MEDIUM);
                        settings.setSupportZoom(true);
                        settings.setLoadWithOverviewMode(true);
                        settings.setJavaScriptEnabled(true);
                        settings.setSaveFormData(true);
                        settings.setSavePassword(true);
                        wvLogin.setVisibility(0);
                        wvLogin.loadUrl(mUri.toString());
                    }
                } catch (Exception e) {
                    Log.e("new mLoginAction", e.toString());
                }
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