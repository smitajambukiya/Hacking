package com.patelapp.Entity;

import io.realm.RealmObject;

/**
 * Created by AndroidDevloper on 2/21/2016.
 */
public class EntityLoginUser extends RealmObject {

    private String r_id;
    private String fname;
    private String lname;
    private String email;
    private String mobile;
    private String dob;
    private String community_type;
    private String community_category;
    private String image;
    private String visiblity;
    private String education;
    private String profession;
    private byte[] imagebyte;

    public byte[] getImagebyte() {
        return imagebyte;
    }

    public void setImagebyte(byte[] imagebyte) {
        this.imagebyte = imagebyte;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVisiblity() {
        return visiblity;
    }

    public void setVisiblity(String visiblity) {
        this.visiblity = visiblity;
    }


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCommunity_type() {
        return community_type;
    }

    public void setCommunity_type(String community_type) {
        this.community_type = community_type;
    }

    public String getCommunity_category() {
        return community_category;
    }

    public void setCommunity_category(String community_category) {
        this.community_category = community_category;
    }




    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;


}
