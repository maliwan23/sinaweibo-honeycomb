package com.lenovo.dll.SinaWeibo;

import weibo4andriod.Weibo;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class OAuthConstant {
    
    private static Weibo weibo=null;
    private static OAuthConstant instance=null;
    private RequestToken requestToken;
    private AccessToken accessToken;
    private String token;
    private String tokenSecret;
    
    private OAuthConstant() {};
    
    public static synchronized OAuthConstant getInstance(){
        if(instance==null)
            instance= new OAuthConstant();
        return instance;
    }
    
    public Weibo getWeibo(){
        if(weibo==null)
            weibo= new Weibo();
        return weibo;
    }
    
    public Weibo init() {
        if (weibo != null) {
            try {
                weibo.endSession();
            } catch (Exception e) {
                Log.e("OAuthConstant", e.toString());
            }
        }
        weibo = new Weibo();
        return weibo;
    }
    
    public AccessToken getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
        this.token=accessToken.getToken();
        this.tokenSecret=accessToken.getTokenSecret();
    }
    
    public RequestToken getRequestToken() {
        return requestToken;
    }
    
    public void setRequestToken(RequestToken requestToken) {
        this.requestToken = requestToken;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTokenSecret() {
        return tokenSecret;
    }
    
    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }
    
    public boolean loadSettings(Context context) {
        try {
            SharedPreferences settings = context.getApplicationContext().getSharedPreferences("SinaWeibo", Context.MODE_WORLD_READABLE);
            token = settings.getString("token", "");
            tokenSecret = settings.getString("tokenSecret", "");
            return (!token.isEmpty() && !tokenSecret.isEmpty());
        } catch (Exception e) {
            Log.e("OAuthConstant", e.toString());
            return false;
        }
    }
    
    public boolean saveSettings(Context context) {
        try {
            SharedPreferences settings = context.getApplicationContext().getSharedPreferences("SinaWeibo", Context.MODE_WORLD_READABLE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("token", token);
            editor.putString("tokenSecret", tokenSecret);
            editor.commit();
        } catch (Exception e) {
            Log.e("OAuthConstant", e.toString());
            return false;
        }
        return true;
    }
}
