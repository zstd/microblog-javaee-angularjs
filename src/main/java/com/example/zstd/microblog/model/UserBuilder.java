package com.example.zstd.microblog.model;

public class UserBuilder {
    private String username;
    private String nickname;
    private String password;
    private String description;
    private String photoUrl;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder withNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public UserBuilder withPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public UserBuilder withNames(String username,String nickname) {
        this.username = username;
        this.nickname = nickname;
        return this;
    }

    public UserBuilder but() {
        return anUser().withUsername(username).withNickname(nickname).withPassword(password).withDescription(description).withPhotoUrl(photoUrl);
    }

    public User build() {
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setPassword(password);
        user.setDescription(description);
        user.setPhotoUrl(photoUrl);
        return user;
    }
}
