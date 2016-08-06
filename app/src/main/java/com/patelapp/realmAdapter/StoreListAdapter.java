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
import com.patelapp.Entity.EntityStoreItem;
import com.patelapp.R;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by AndroidDevloper on 2/28/2016.
 */
public class StoreListAdapter extends RealmBaseAdapter<EntityStoreItem> implements ListAdapter {
    public StoreListAdapter(Context context, RealmResults<EntityStoreItem> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    private static class ViewHolder {
        TextView tvItemName, tvItemPrice, tvItemDesc, tvPostDate;
        ImageView ivStoreItem;
        LinearLayout lnAnim;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_store, parent, false);
            holder.ivStoreItem = (ImageView) convertView.findViewById(R.id.ivStoreItem);
            holder.tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
            holder.tvItemPrice = (TextView) convertView.findViewById(R.id.tvItemPrice);
            holder.tvItemDesc = (TextView) convertView.findViewById(R.id.tvItemDesc);
            holder.tvPostDate = (TextView) convertView.findViewById(R.id.tvPostDate);
            holder.lnAnim = (LinearLayout) convertView.findViewById(R.id.lnAnim);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        EntityStoreItem item = getItem(position);
        holder.tvItemName.setText(item.getItem_name());
        holder.tvItemDesc.setText(item.getItem_desc());
        holder.tvItemPrice.setText(item.getPrice());
        String date =  StaticMethodUtility.getDisplayDate(item.getCreated());
        holder.tvPostDate.setText(date);
        Glide.with(context).load(Const.ImagePath + item.getUrl1()).placeholder(R.drawable.no_image_user).centerCrop().into(((ViewHolder) holder).ivStoreItem);

        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.card_animation);
        holder.lnAnim.startAnimation(animation);


        return convertView;
    }
}
