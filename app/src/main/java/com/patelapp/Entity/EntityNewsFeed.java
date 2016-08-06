package com.patelapp.Entity;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by AndroidDevloper on 2/7/2016.
 */
public class EntityNewsFeed extends RealmObject  {
    @PrimaryKey
    private String nf_id;
    private String r_category;
    private String title;

    private String desc;

    private String img_url;

    private String view ;

    private String post_by;

    private String created;
    private String is_like;

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getNf_id() {
        return nf_id;
    }

    public void setNf_id(String nf_id) {
        this.nf_id = nf_id;
    }

    public String getR_category() {
        return r_category;
    }

    public void setR_category(String r_category) {
        this.r_category = r_category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getPost_by() {
        return post_by;
    }

    public void setPost_by(String post_by) {
        this.post_by = post_by;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
