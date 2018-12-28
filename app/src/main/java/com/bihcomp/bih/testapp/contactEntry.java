package com.bihcomp.bih.testapp;

public class contactEntry {
    private String name;
    private String phoneNo;
    private int photo;

    public contactEntry(String _name, String _pn, int _photo) {
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
