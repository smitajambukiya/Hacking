package com.patelapp.realmAdapter;

import android.content.Context;
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
import com.patelapp.Entity.EntityDirectory;
import com.patelapp.Entity.EntityUser;
import com.patelapp.R;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by AndroidDevloper on 2/18/2016.
 */
public class DirectoryAdapter extends RealmBaseAdapter<EntityDirectory> implements ListAdapter {
    Typeface face;
    public DirectoryAdapter(Context context, RealmResults<EntityDirectory> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        face = Typeface.createFromAsset(context.getAssets(),
                "fonts/"+context.getString(R.string.font_guj));

    }

    private static class ViewHolder {
        TextView tvTitle, tvType, tvAddress;
        ImageView ivUser;
        LinearLayout lnAnim;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_directory, parent, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvType = (TextView) convertView.findViewById(R.id.tvType);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.ivUser = (ImageView) convertView.findViewById(R.id.ivDirectory);
            holder.lnAnim = (LinearLayout) convertView.findViewById(R.id.lnAnim);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        EntityDirectory item = getItem(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvType.setText(item.getType());
        holder.tvAddress.setText(item.getAddress());

        holder.tvType.setTypeface(face);
        holder.tvTitle.setTypeface(face);
        holder.tvAddress.setTypeface(face);
        Glide.with(context).load(Const.ImagePath + item.getImage()).centerCrop().into(((ViewHolder) holder).ivUser);

        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.card_animation);
        holder.lnAnim.startAnimation(animation);

        return convertView;
    }

    public RealmResults<EntityDirectory> getRealmResults() {
        return realmResults;
    }

}


