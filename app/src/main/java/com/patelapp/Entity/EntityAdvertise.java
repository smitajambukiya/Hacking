package com.patelapp.Entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by AndroidDevloper on 3/6/2016.
 */
public class EntityAdvertise extends RealmObject {

    @PrimaryKey
    private String a_id;
    private String title;
    private String contactperson;
    private String mobile;
    private String a_desc;
    private String img_small;
    private String img_large;

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getA_desc() {
        return a_desc;
    }

    public void setA_desc(String a_desc) {
        this.a_desc = a_desc;
    }

    public String getImg_small() {
        return img_small;
    }

    public void setImg_small(String img_small) {
        this.img_small = img_small;
    }

    public String getImg_large() {
        return img_large;
    }

    public void setImg_large(String img_large) {
        this.img_large = img_large;
    }
}
