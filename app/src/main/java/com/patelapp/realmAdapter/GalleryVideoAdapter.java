package com.patelapp.realmAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.patelapp.Entity.EntityGallery;
import com.patelapp.R;
import com.patelapp.VideoDetailsActivity;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by AndroidDevloper on 2/21/2016.
 */
public class GalleryVideoAdapter extends RealmBaseAdapter<EntityGallery> {

    Typeface face;

    public GalleryVideoAdapter(Context context, RealmResults<EntityGallery> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        face = Typeface.createFromAsset(context.getAssets(),
                "fonts/"+context.getString(R.string.font_guj));

    }

    private static class ViewHolder {
        ImageView ivGalleryVideo;
        TextView tvVideoTitle;
        LinearLayout lnClick;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_video_item, parent, false);
            holder.ivGalleryVideo = (ImageView) convertView.findViewById(R.id.ivGalleryVideo);
            holder.tvVideoTitle = (TextView) convertView.findViewById(R.id.tvVideoTitle);
            holder.lnClick = (LinearLayout) convertView.findViewById(R.id.lnClick);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final EntityGallery item = getItem(position);
        holder.tvVideoTitle.setText(item.getTitle());
        holder.tvVideoTitle.setTypeface(face);
        holder.lnClick.setBackgroundResource(R.drawable.home);



        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.card_animation);
        holder.lnClick.startAnimation(animation);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoDetailsActivity.class);
                intent.putExtra("key_gallery_id", item.getG_id());
                context.startActivity(intent);

            }
        });

//        holder.ivGalleryVideo.setBackgroundColor(Color.RED);

        return convertView;
    }

    public RealmResults<EntityGallery> getRealmResults() {
        return realmResults;
    }


}
