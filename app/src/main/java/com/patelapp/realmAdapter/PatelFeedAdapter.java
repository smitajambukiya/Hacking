package com.patelapp.realmAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
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
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.Entity.EntityNewsFeed;
import com.patelapp.R;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by AndroidDevloper on 2/9/2016.
 */

public class PatelFeedAdapter extends RealmBaseAdapter<EntityNewsFeed> implements ListAdapter {

    int img_width,img_height;
    Typeface face;
    public PatelFeedAdapter(Context context, RealmResults<EntityNewsFeed> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        face = Typeface.createFromAsset(context.getAssets(),
                "fonts/"+context.getString(R.string.font_guj));
    }

    private static class ViewHolder {
        TextView tvTitle, tvDesc, tvDate;
        ImageView ivFeed;
        LinearLayout lnAnim;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_patel_feed, parent, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            holder.ivFeed = (ImageView) convertView.findViewById(R.id.ivFeed);
            holder.lnAnim = (LinearLayout) convertView.findViewById(R.id.lnAnim);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EntityNewsFeed feed = realmResults.get(position);
        holder.tvTitle.setText(feed.getTitle());
        String Desc = feed.getDesc().toString().replaceAll("^\\s+", "");
        holder.tvDesc.setText(Desc);
        String date =  StaticMethodUtility.getDisplayDate(feed.getCreated());
        holder.tvDate.setText(date);

        holder.tvDate.setTypeface(face);
        holder.tvTitle.setTypeface(face);
        holder.tvDesc.setTypeface(face);

        Log.d("image width",""+holder.ivFeed.getWidth());

        Glide.with(context).load(Const.ImagePath + feed.getImg_url())
                .crossFade().centerCrop()
                .into(((ViewHolder) holder).ivFeed);

        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.card_animation);
        holder.lnAnim.startAnimation(animation);

        return convertView;
    }

    public RealmResults<EntityNewsFeed> getRealmResults() {
        return realmResults;
    }

}
