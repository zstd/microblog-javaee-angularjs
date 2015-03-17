package com.example.zstd.microblog.dto;

public class RegistrationDataBuilder {
    private String username;
    private String nickname;
    private String password;
    private String passwordCheck;
    private String description;
    private String photoUrl;

    private RegistrationDataBuilder() {
    }

    public static RegistrationDataBuilder aRegistrationData() {
        return new RegistrationDataBuilder();
    }

    public RegistrationDataBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public RegistrationDataBuilder withNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public RegistrationDataBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public RegistrationDataBuilder withPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
        return this;
    }

    public RegistrationDataBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public RegistrationDataBuilder withPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public RegistrationData build() {
        RegistrationData registrationData =
                new RegistrationData(username, nickname, password, passwordCheck, description, photoUrl);
        return registrationData;
    }
}
