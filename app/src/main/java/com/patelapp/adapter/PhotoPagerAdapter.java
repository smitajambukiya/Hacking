package com.patelapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.Entity.EntityGallery;
import com.patelapp.R;

import java.util.ArrayList;


public class PhotoPagerAdapter extends PagerAdapter {

    private Activity _activity;
    private LayoutInflater inflater;
    ArrayList<EntityGallery> alPhotos;
    Typeface face;
   public interface OnShareListener{
        void onShareClick(int pos);
    }
    OnShareListener onShareListener;

    // constructor
    public PhotoPagerAdapter(Activity activity, ArrayList<EntityGallery> imageIdList) {
        this._activity = activity;
        this.alPhotos = imageIdList;
        face = Typeface.createFromAsset(_activity.getAssets(),
                "fonts/"+_activity.getString(R.string.font_guj));
    }

    public  void setListener(OnShareListener _onShareListener){
        this.onShareListener = _onShareListener;
    }
    @Override
    public int getCount() {
        return alPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ScrollView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.grid_item_photo, container,
                false);
        ImageView ivFooter = (ImageView) viewLayout.findViewById(R.id.ivPhoto);

        TextView tvDate = (TextView) viewLayout.findViewById(R.id.tvDate);
        TextView tvTitle = (TextView) viewLayout.findViewById(R.id.tvTitle);
        TextView tvLocation = (TextView) viewLayout.findViewById(R.id.tvLocation);
        TextView tvDesc = (TextView) viewLayout.findViewById(R.id.tvDesc);
        LinearLayout lnAnim = (LinearLayout) viewLayout.findViewById(R.id.lnAnim);
        FloatingActionButton fabShare = (FloatingActionButton)viewLayout.findViewById(R.id.fabShare);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareListener.onShareClick(position);
            }
        });
        String image_path = alPhotos.get(position).getImg_video();
        Glide.with(_activity).load(Const.ImagePath + image_path).fitCenter().into(ivFooter);

        String display_date = StaticMethodUtility.getDisplayDate(alPhotos.get(position).getCreated());
        tvDate.setText(display_date);

        tvTitle.setText(alPhotos.get(position).getTitle());
        tvLocation.setText(alPhotos.get(position).getLocation());
        tvDesc.setText(alPhotos.get(position).getG_desc());

        tvTitle.setTypeface(face);
        tvLocation.setTypeface(face);
        tvDesc.setTypeface(face);

        Animation animation = AnimationUtils.loadAnimation(_activity,
                R.anim.card_animation);
        lnAnim.startAnimation(animation);

        container.addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((ScrollView) object);


    }


}
