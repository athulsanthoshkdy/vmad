package com.coduniq.dmad.ui.home;

public class User {
    private String imageurl,uid,name,contact,blood,health;

    public User() {
    }

    public User(String imageurl, String uid, String name, String contact, String blood, String health) {
        this.imageurl = imageurl;
        this.uid = uid;
        this.name = name;
        this.contact = contact;
        this.blood = blood;
        this.health = health;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }
}
