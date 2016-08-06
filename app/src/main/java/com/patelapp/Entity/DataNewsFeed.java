package com.patelapp.Entity;

import java.io.Serializable;

/**
 * Created by Android on 1/3/2016.
 */
public class DataNewsFeed implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String emailId;

    public DataNewsFeed() {

    }

    public DataNewsFeed(String name, String emailId) {
        this.name = name;
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }


}

