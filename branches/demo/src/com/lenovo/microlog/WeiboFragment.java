package com.lenovo.microlog;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WeiboFragment extends Fragment {

    private View viewer = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewer = inflater.inflate(R.layout.weibo_view, container, false);
        return viewer;
    }

}
