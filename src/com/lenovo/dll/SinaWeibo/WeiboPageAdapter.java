package com.lenovo.dll.SinaWeibo;

import java.util.List;
import java.util.regex.Pattern;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeiboPageAdapter extends BaseAdapter {
    private Activity activity;
    private List <String> profileImageData;
    private List <String> textset;
    private List <String> middleImageData;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    
    public WeiboPageAdapter(Activity a, List<String> d, List<String> ts, List<String> img) {
        activity = a;
        profileImageData=d;
        textset = ts;
        middleImageData=img;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
    
    public int getCount() {
        return profileImageData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public static class ViewHolder{
        public TextView text;
        public ImageView profileImage;
        public ImageView middleImage;
        
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        
        View vi=convertView;
        ViewHolder holder;
        
        if(convertView==null){
            vi = inflater.inflate(R.layout.list_view, null);
            holder=new ViewHolder();
            holder.text=(TextView)vi.findViewById(R.id.text);;
            holder.profileImage=(ImageView)vi.findViewById(R.id.image);
            holder.middleImage=(ImageView)vi.findViewById(R.id.image_middle);
            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();
        
        holder.text.setText(Html.fromHtml(textset.get(position)));
        holder.profileImage.setTag(profileImageData.get(position));
        holder.middleImage.setTag(middleImageData.get(position));
        imageLoader.DisplayImage(profileImageData.get(position), activity, holder.profileImage);
        if (middleImageData.get(position).equals("NoPhotos"))
        {
            // do nothing
            holder.middleImage.setVisibility(View.GONE);
        }
        imageLoader.DisplayImage(middleImageData.get(position), activity, holder.middleImage);  
        
        Pattern pattern = Pattern.compile("video://\\S+");
        Linkify.addLinks(holder.text, pattern, "video://");
        
        return vi;
    }
    
}
