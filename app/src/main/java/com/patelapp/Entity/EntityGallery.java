package com.patelapp.Entity;

import io.realm.RealmObject;

/**
 * Created by AndroidDevloper on 2/21/2016.
 */
public class EntityGallery extends RealmObject {
    private String g_id= "";
    private String  title= "";
    private String e_datetime= "";
    private String g_desc= "";
    private String location = "";
    private String  type= "";
    private String  img_video= "";
    private String created;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getE_datetime() {
        return e_datetime;
    }

    public void setE_datetime(String e_datetime) {
        this.e_datetime = e_datetime;
    }

    public String getG_desc() {
        return g_desc;
    }

    public void setG_desc(String g_desc) {
        this.g_desc = g_desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_video() {
        return img_video;
    }

    public void setImg_video(String img_video) {
        this.img_video = img_video;
    }


}
