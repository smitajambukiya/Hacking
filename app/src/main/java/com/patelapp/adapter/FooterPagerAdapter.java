package com.patelapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.bumptech.glide.Glide;
import com.patelapp.AdvertiseDetailActvity;
import com.patelapp.Custom.Const;
import com.patelapp.Entity.EntityAdvertise;
import com.patelapp.R;


public class FooterPagerAdapter extends PagerAdapter {

    private Activity _activity;
    private LayoutInflater inflater;
    private ArrayList<EntityAdvertise> alAdv;


    // constructor
    public FooterPagerAdapter(Activity activity, ArrayList<EntityAdvertise> imageIdList) {
        this._activity = activity;
        this.alAdv = imageIdList;

    }

    @Override
    public int getCount() {
        return alAdv.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.pager_footer, container,
                false);
        ImageView ivFooter = (ImageView) viewLayout.findViewById(R.id.ivFooter);
        LinearLayout lnAdd = (LinearLayout) viewLayout.findViewById(R.id.lnAdd);


        EntityAdvertise item = alAdv.get(position);
        ((ViewPager) container).addView(viewLayout);
        lnAdd.setTag(item);

        Glide.with(_activity).load(Const.ImagePath+item.getImg_small()).fitCenter().into(ivFooter);

        lnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EntityAdvertise item = (EntityAdvertise) v.getTag();
                Intent in = new Intent(_activity, AdvertiseDetailActvity.class);
                in.putExtra("image_url",item.getImg_large());
                in.putExtra("a_id",item.getA_id());
                _activity.startActivity(in);
            }
        });
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);

    }

}
