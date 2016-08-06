package com.patelapp.Entity;

import android.content.Intent;

/**
 * Created by AndroidDevloper on 2/29/2016.
 */
public class InterfaceModel {

    public static InterfaceModel mInstance1;
    public static InterfaceModel mInstance2;

    OnTabChangeListener mListener;

    public InterfaceModel(){

    }


    public static  InterfaceModel getInstance(int pos){
        if(pos == 0){
            if(mInstance1 == null) {
                mInstance1 = new InterfaceModel();
            }
            return mInstance1;
        }else if(pos == 1)
        {
            if(mInstance2 == null) {
                mInstance2 = new InterfaceModel();
            }
            return mInstance2;
        }
        return mInstance1;
    }



    public void setListeners(OnTabChangeListener onTabChangeListener){
        this.mListener = onTabChangeListener;
    }


    public void OnTabLoaded(){
        if(mListener != null)
            mListener.onTabLoaded();
    }

    public void OnSearchCalled(String search_term){
        if(mListener != null)
            mListener.onSearchCalled(search_term);
    }
    public interface OnTabChangeListener{

        void onTabLoaded();
        void onSearchCalled(String search_text);
    }

}
