package com.example.atry.model;

public class UserModel {

    private int id;
    private String firstName;
    private String email;

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    private String urlAvatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserModel(int id, String firstName, String email, String urlAvatar) {
        this.id = id;
        this.firstName = firstName;
        this.email = email;
        this.urlAvatar = urlAvatar;
    }
}
