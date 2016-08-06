package com.patelapp.Custom;

import com.patelapp.Entity.EntityAdvertise;
import com.patelapp.Entity.EntityDirectory;
import com.patelapp.Entity.EntityGallery;
import com.patelapp.Entity.EntityLoginUser;
import com.patelapp.Entity.EntityNewsFeed;
import com.patelapp.Entity.EntityStoreItem;
import com.patelapp.Entity.EntityUser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Entity;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by AndroidDevloper on 2/10/2016.
 */
public class DataAccessManager {



/*
CRUD Login User
 */

    public static void insertLoginUser(JSONObject user_object) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(EntityLoginUser.class).findAll().clear();
        realm.createObjectFromJson(EntityLoginUser.class, user_object);
        realm.commitTransaction();
    }

public static void updateLoginUserImage(final byte[] image_arr){
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            EntityLoginUser user = realm.where(EntityLoginUser.class).findFirst();
            user.setImagebyte(image_arr);
        }
    });

}
    public static EntityLoginUser getLoginUser() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        EntityLoginUser mLoginUser = realm.where(EntityLoginUser.class).findFirst();
        realm.commitTransaction();
        return mLoginUser;
    }

    public static void DeleteLoginUser() {
        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        realm.where(EntityLoginUser.class).findAll().clear();
//        realm.commitTransaction();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(EntityLoginUser.class).findAll().clear();
            }
        });

    }

    /*
       CRUD Patel Feed
     */
    public static void insertPatelFeed(final JSONArray jsonArray) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(EntityNewsFeed.class).findAll().clear();
        realm.createAllFromJson(EntityNewsFeed.class, jsonArray);
        realm.commitTransaction();
    }

    public static EntityNewsFeed getPatelFeed(String feed_id) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityNewsFeed> mQueryFeed = realm.where(EntityNewsFeed.class).equalTo("nf_id", feed_id);
        EntityNewsFeed mFeed = mQueryFeed.findFirst();
        realm.commitTransaction();
        return mFeed;
    }

    public static void updatePatelFeed(final JSONArray jsonArray) {
        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        realm.createAllFromJson(EntityNewsFeed.class, jsonArray);
//        realm.commitTransaction();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(EntityNewsFeed.class, jsonArray);
            }
        });

    }

    public static RealmResults<EntityNewsFeed> getPatelFeed() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<EntityNewsFeed> results = realm.where(EntityNewsFeed.class).findAll();
        realm.commitTransaction();
        return results;
    }
    public static  void updateFeedLike(String nf_id){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        EntityNewsFeed mFeed = realm.where(EntityNewsFeed.class).equalTo("nf_id",nf_id).findFirst();
        mFeed.setIs_like("1");
        int view_count = Integer.parseInt(mFeed.getView()) +1;
        mFeed.setView(String.valueOf(view_count));
        realm.copyToRealmOrUpdate(mFeed);
        realm.commitTransaction();
    }

    /*
       CRUD User Directory
     */

    public static void insertDirectory(final JSONArray jsonArray, String city) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(EntityDirectory.class).contains("city", city,Case.INSENSITIVE).findAll().clear();
        realm.createAllFromJson(EntityDirectory.class, jsonArray);
        realm.commitTransaction();
    }


    public static RealmResults<EntityDirectory> getDirectoryList(String city) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityDirectory> realmQueryUser = realm.where(EntityDirectory.class).contains("city", city,Case.INSENSITIVE);
        RealmResults<EntityDirectory> results = realmQueryUser.findAll();
        realm.commitTransaction();
        return results;
    }

    public static void updateDirectory(final JSONArray jsonArray) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.createAllFromJson(EntityDirectory.class, jsonArray);
        realm.commitTransaction();
    }

    public static EntityDirectory getDirectoryDetail(String d_id) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityDirectory> mQueryFeed = realm.where(EntityDirectory.class).equalTo("d_id", d_id);
        EntityDirectory mFeed = mQueryFeed.findFirst();
        realm.commitTransaction();
        return mFeed;
    }


    /*
    CRUD User Table
     */

    public static void insertUser(final JSONArray jsonArray, String community_type) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(EntityUser.class).equalTo("community_type", community_type).findAll().clear();
        realm.createAllFromJson(EntityUser.class, jsonArray);
        realm.commitTransaction();
    }

    public static EntityUser getUserDetail(String r_id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityUser> mQueryFeed = realm.where(EntityUser.class).equalTo("r_id", r_id);
        EntityUser mFeed = mQueryFeed.findFirst();
        realm.commitTransaction();
        return mFeed;
    }

    public static RealmResults<EntityUser> getUserList(String commn_type) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityUser> realmQueryUser = realm.where(EntityUser.class).equalTo("community_type", commn_type);
        RealmResults<EntityUser> results = realmQueryUser.findAll();
        realm.commitTransaction();
        return results;
    }

    public static RealmResults<EntityUser> getUserSearchList(String fname,String commn_type) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityUser> realmQueryUser = realm.where(EntityUser.class).contains("fname", fname, Case.INSENSITIVE).equalTo("community_type",commn_type);
        RealmResults<EntityUser> results = realmQueryUser.findAll();
        realm.commitTransaction();
        return results;
    }


    public static void updateUser(final JSONArray jsonArray) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.createOrUpdateAllFromJson(EntityUser.class, jsonArray);
        realm.commitTransaction();
    }


    /*
     *    CRUD Gallery Table
     */

    public static void insertGallery(final JSONArray jsonArray,String type) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(EntityGallery.class).equalTo("type",type).findAll().clear();
        realm.createAllFromJson(EntityGallery.class, jsonArray);
        realm.commitTransaction();
    }

    public static RealmResults<EntityGallery> getGalleryList(String gallery_type) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityGallery> realmQueryUser = realm.where(EntityGallery.class).equalTo("type", gallery_type);
        RealmResults<EntityGallery> results = realmQueryUser.findAll();
        realm.commitTransaction();
        return results;
    }

    public static EntityGallery getGalleryRecords(String gallery_id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityGallery> realmQueryUser = realm.where(EntityGallery.class).equalTo("g_id", gallery_id);
        EntityGallery entityGallery = realmQueryUser.findFirst();
        realm.commitTransaction();
        return entityGallery;
    }


    public static ArrayList<EntityGallery> getAllGallery(String type) {
        ArrayList<EntityGallery> alItems = new ArrayList<EntityGallery>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityGallery> realmQueryUser = realm.where(EntityGallery.class).equalTo("type", type);
        RealmResults<EntityGallery> results = realmQueryUser.findAll();
        for (int i = 0; i < results.size(); i++) {
            alItems.add(results.get(i));
        }
        realm.commitTransaction();
        return alItems;
    }


    /*
    * CRUD Store table
    * */

    public static void insertAllStore(final JSONArray jsonArray) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        // realm.where(EntityStoreItem.class).notEqualTo("register_id",register_id).findAll().clear();
        realm.where(EntityStoreItem.class).findAll().clear();
        realm.createAllFromJson(EntityStoreItem.class, jsonArray);
        realm.commitTransaction();
    }

    public static RealmResults<EntityStoreItem> getAllStore(String register_id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityStoreItem> realmQueryUser = realm.where(EntityStoreItem.class).notEqualTo("register_id", register_id);
        RealmResults<EntityStoreItem> results = realmQueryUser.findAll();
        realm.commitTransaction();
        return results;
    }


    public static void insertMyStore(final JSONArray jsonArray, String register_id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(EntityStoreItem.class).equalTo("register_id", register_id).findAll().clear();
        realm.createAllFromJson(EntityStoreItem.class, jsonArray);
        realm.commitTransaction();
    }

    public static RealmResults<EntityStoreItem> getMyStore(String register_id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityStoreItem> realmQueryUser = realm.where(EntityStoreItem.class).equalTo("register_id", register_id);
        RealmResults<EntityStoreItem> results = realmQueryUser.findAllSorted("id", Sort.DESCENDING);
        realm.commitTransaction();
        return results;
    }

    public static void updateStore(JSONObject jsonObject) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.createObjectFromJson(EntityStoreItem.class, jsonObject);
        realm.commitTransaction();
    }
    public static EntityStoreItem getStoreRecord(String store_id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityStoreItem> realmQueryUser = realm.where(EntityStoreItem.class).equalTo("id", store_id);
        EntityStoreItem entityStore = realmQueryUser.findFirst();
        realm.commitTransaction();
        return entityStore;
    }

    public static void updateStore(final JSONArray jsonArray) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.createOrUpdateAllFromJson(EntityStoreItem.class, jsonArray);
        realm.commitTransaction();
    }

    public  static  void removeStore(final String id){

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(EntityStoreItem.class).equalTo("id", id).findFirst().removeFromRealm();


            }
        });

    }

    public static RealmResults<EntityStoreItem> getStoreSearchList(String item_name,String register_id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityStoreItem> realmQueryUser = realm.where(EntityStoreItem.class).contains("item_name", item_name, Case.INSENSITIVE).notEqualTo("register_id", register_id);;
        RealmResults<EntityStoreItem> results = realmQueryUser.findAll();
        realm.commitTransaction();
        return results;
    }


    public static RealmResults<EntityStoreItem> getMyStoreSearchList(String item_name,String register_id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmQuery<EntityStoreItem> realmQueryUser = realm.where(EntityStoreItem.class).contains("item_name", item_name, Case.INSENSITIVE).equalTo("register_id", register_id);;
        RealmResults<EntityStoreItem> results = realmQueryUser.findAll();
        realm.commitTransaction();
        return results;
    }




    /*
    * CRUD Advertise
    * */

    public static void insertAdvertise(final JSONArray jsonArray) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(EntityAdvertise.class).findAll().clear();
        realm.createAllFromJson(EntityAdvertise.class, jsonArray);
        realm.commitTransaction();
    }

    public static ArrayList<EntityAdvertise> getAdvertise() {

        ArrayList<EntityAdvertise> alAdvertise = new ArrayList<EntityAdvertise>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<EntityAdvertise> realmResults = realm.where(EntityAdvertise.class).findAll();
        for(int i = 0; i < realmResults.size() ;  i++){
            alAdvertise.add(realmResults.get(i));
        }
        realm.commitTransaction();

    return  alAdvertise;
    }

    public static EntityAdvertise getAdvertiseItem(final String advertise_id) {

        EntityAdvertise advertise  = new EntityAdvertise();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        advertise =  realm.where(EntityAdvertise.class).equalTo("a_id",advertise_id).findFirst();
        realm.commitTransaction();
        return  advertise;
    }

}


