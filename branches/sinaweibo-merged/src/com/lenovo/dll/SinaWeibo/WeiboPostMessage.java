package com.lenovo.dll.SinaWeibo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.http.ImageItem;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class WeiboPostMessage extends Fragment {
    
    private EditText text;
    private Button button;
    private Button capture;
    private ImageView image;
    private Status status;
    private String UrlString;
    private String inputContent;
    
    private Weibo weibo;
    private ImageItem picture;
    private View view = null;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.postmessage, container, false);
        hide();
        
        text = (EditText) view.findViewById(R.id.entry);
        image = (ImageView) view.findViewById(R.id.imgSnapshot2);
        capture = (Button) view.findViewById(R.id.btnCaptureVideo);

        button = (Button) view.findViewById(R.id.ok);
        button.setOnClickListener(new OnClickListener(){
            
            public void onClick(View v) {
                
                if (text.getText().length() == 0)
                {
                    Toast.makeText(view.getContext(), "Please type something new.", Toast.LENGTH_SHORT).show();
                } 
                else 
                {
                    try {
                        new PostTask().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }           
        });
        return view;
    }

    public void ImageGenerated() {
        try {
            byte[] content= readFileImage("/sdcard/snapshot.jpg");
            
            if(content.length <= 0){
                System.out.println("Image file is null");
            }
            
            picture=new ImageItem("pic",content);
            UrlString = URLEncoder.encode(inputContent,"UTF-8");
        } catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
            throw new IOException("The file you access is not Existed.");
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
    
    class PostTask extends AsyncTask<Void, Void, Boolean> {
        
        @Override
        protected void onPreExecute() {
            text.setEnabled(false);
            button.setEnabled(false);
            capture.setEnabled(false);
        }
    
        @Override
        protected Boolean doInBackground(Void... params) {
            
            try {
                inputContent = text.getText().toString();
                inputContent += " " + VideoFragment.videoPath;
                
                weibo = OAuthConstant.getInstance().getWeibo();

                ImageGenerated();
                status = weibo.uploadStatus(UrlString, picture);
                
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        
        @Override
        protected void onPostExecute(final Boolean success) {
            text.setEnabled(true);
            button.setEnabled(true);
            capture.setEnabled(true);
            if (success) {
                if ( status.getText() != null && !status.getText().equals("") )
                {
                    text.setText("");
                    image.setImageBitmap(null);
                    
                    Toast.makeText(view.getContext(), "Post message successfully.",
                        Toast.LENGTH_LONG).show();

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("weibo4andriod://UpdateHomepage"));
                    startActivity(i);
                    return;
                }
            }
            Toast.makeText(view.getContext(), "Failed to post message, please try again later.",
                    Toast.LENGTH_LONG).show();
        }
    }
}

