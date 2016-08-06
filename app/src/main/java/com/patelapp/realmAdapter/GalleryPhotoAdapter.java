package com.patelapp.realmAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.patelapp.Custom.Const;
import com.patelapp.Entity.EntityNewsFeed;
import com.patelapp.Entity.EntityGallery;
import com.patelapp.PhotoDetailsActivity;
import com.patelapp.R;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by AndroidDevloper on 2/16/2016.
 */
public class GalleryPhotoAdapter extends RealmBaseAdapter<EntityGallery> implements ListAdapter {

    Typeface face;
    public GalleryPhotoAdapter(Context context, RealmResults<EntityGallery> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        face = Typeface.createFromAsset(context.getAssets(),
                "fonts/"+context.getString(R.string.font_guj));


    }

    private static class ViewHolder {
        ImageView ivGallery;
        TextView tvTitle;
        LinearLayout lnAnim;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_gallery_item, parent, false);
            holder.ivGallery = (ImageView) convertView.findViewById(R.id.ivGallery);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.lnAnim = (LinearLayout) convertView.findViewById(R.id.lnAnim);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        EntityGallery item = getItem(position);
        Glide.with(context).load(Const.ImagePath + item.getImg_video()).centerCrop().into(((ViewHolder) holder).ivGallery);

        holder.tvTitle.setText(item.getTitle());
        holder.tvTitle.setTypeface(face);
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.card_animation);
        holder.lnAnim.startAnimation(animation);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoDetailsActivity.class);
                intent.putExtra("key_position", position);
                context.startActivity(intent);
            }
        });

//        holder.lnAnim

        return convertView;
    }

    public RealmResults<EntityGallery> getRealmResults() {
        return realmResults;
    }

}

