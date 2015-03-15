package com.example.zstd.microblog.model;

/**
 * Model class representing one user of blog.
 */
public class User {

    public static final String DB_FIELD_USERNAME = "user_name";
    public static final String DB_FIELD_PASSWORD = "password";
    public static final String DB_FIELD_NICKNAME = "nickname";
    public static final String DB_FIELD_DESCRIPTION = "description";
    public static final String DB_FIELD_PHOTO_URL = "photo_url";

    public static final String[] DB_FIELDS = new String[]{
            User.DB_FIELD_USERNAME, User.DB_FIELD_PASSWORD, User.DB_FIELD_NICKNAME,
            User.DB_FIELD_DESCRIPTION, User.DB_FIELD_PHOTO_URL};
    public User() {}

    public User(String username, String nickname, String password,
                String description, String photoUrl) {
        super();
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.description = description;
        this.photoUrl = photoUrl;
    }

    private String username;
    private String nickname;
    private String password;
    private String description;
    private String photoUrl;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "BlogUser ["
                + "username='" + username + "', "
                + "nickname='" + nickname + "', "
                + "password='" + password + "', "
                + "description='" + description + "', "
                + "photoUrl='" + photoUrl
                + "']";
    }



}