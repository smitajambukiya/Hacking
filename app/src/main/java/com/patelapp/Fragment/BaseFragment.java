package com.patelapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.patelapp.MainActivity;

/**
 * Created by Android on 12/30/2015.
 */
public class BaseFragment extends Fragment {
protected   MainActivity mActivity ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity)this.getActivity();
    }


}
