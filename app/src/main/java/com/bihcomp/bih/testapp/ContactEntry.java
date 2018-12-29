package com.bihcomp.bih.testapp;

public class ContactEntry {
    private String name;
    private String phoneNo;
    private int photo;

    public ContactEntry(String _name, String _pn, int _photo) {
        this.name = _name;
        this.phoneNo = _pn;
        this.photo = _photo;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public int getPhotoId() {
        return photo;
    }
}
