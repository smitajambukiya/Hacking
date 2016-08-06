package com.patelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.ExtendedViewPager;
import com.patelapp.Custom.TouchImageView;

import java.util.ArrayList;

/**
 * Created by AndroidDevloper on 3/6/2016.
 */
public class ZoomImageActivity extends  BaseActivity {


    /**
     * Step 1: Download and set up v4 support library: http://developer.android.com/tools/support-library/setup.html
     * Step 2: Create ExtendedViewPager wrapper which calls TouchImageView.canScrollHorizontallyFroyo
     * Step 3: ExtendedViewPager is a custom view and must be referred to by its full package name in XML
     * Step 4: Write TouchImageAdapter, located below
     * Step 5. The ViewPager in the XML should be ExtendedViewPager
     */

    private String[] image_array = new String[3];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);

        if(getIntent().getExtras() != null)
        {
            image_array = getIntent().getExtras().getStringArray("array");
            int pos = getIntent().getExtras().getInt("position");

            mViewPager.setAdapter(new TouchImageAdapter(ZoomImageActivity.this,image_array));
            mViewPager.setCurrentItem(pos);
        }


    }


    static class TouchImageAdapter extends PagerAdapter {



        Context context;
        private  String[] my_arr = new String[3];
        public TouchImageAdapter(Context conext,String[] arr){
            this.context = conext;
            this.my_arr = arr;
        }


        @Override
        public int getCount() {
        return  my_arr.length;
        }


        @Override
        public View instantiateItem(ViewGroup container, int position) {
            TouchImageView img = new TouchImageView(container.getContext());
            //img.setImageResource(images[position]);
            Glide.with(context).load(my_arr[position]).centerCrop().into(img);


            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return img;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }
}
