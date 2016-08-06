package com.patelapp.realmAdapter;

import android.content.Context;
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
import com.patelapp.Entity.EntityUser;
import com.patelapp.R;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by AndroidDevloper on 2/16/2016.
 */
public class UserListAdapter extends RealmBaseAdapter<EntityUser> implements ListAdapter {

    public UserListAdapter(Context context, RealmResults<EntityUser> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    private static class ViewHolder {
        TextView tvName, tvSociety, tvDob;
        ImageView ivUser;
        LinearLayout lnAnim;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_user_list, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvSociety = (TextView) convertView.findViewById(R.id.tvSociety);
            holder.tvDob = (TextView) convertView.findViewById(R.id.tvDob);
            holder.ivUser = (ImageView) convertView.findViewById(R.id.ivUser);
            holder.lnAnim = (LinearLayout) convertView.findViewById(R.id.lnAnim);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        EntityUser item = getItem(position);

        holder.tvName.setText(item.getFname() + " " + item.getLname());

        String[]arr_catgory = context.getResources().getStringArray(R.array.community_categaory);
        String cat = item.getCommunity_category().toString();
        int indext_cat = Integer.parseInt(cat);

        String strCat = arr_catgory[indext_cat +1];
        holder.tvSociety.setText(strCat + " " + item.getCommunity_type() + " samaj");
        holder.tvDob.setText(item.getDob());

        String date = item.getDob();
        if(!date.equalsIgnoreCase(""))
            holder.tvDob.setText(StaticMethodUtility.getDisplayDate(date));

        Glide.with(context).load(Const.ImagePath + item.getImage()).centerCrop().into(((ViewHolder) holder).ivUser);
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.card_animation);
        holder.lnAnim.startAnimation(animation);

        return convertView;
    }

    public RealmResults<EntityUser> getRealmResults() {
        return realmResults;
    }

}

