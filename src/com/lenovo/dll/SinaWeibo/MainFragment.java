package com.lenovo.dll.SinaWeibo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {

    private View viewer = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewer = inflater.inflate(R.layout.main_view, container, false);
        return viewer;
    }

    public void show()
    {
        try {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
//          ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_in);
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
//          ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            ft.hide(this);
            ft.commit();
        } catch (Exception ex) {
            Log.e(this.toString(), ex.toString());
        }
    }

}
