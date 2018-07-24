package com.piyush.interactivestory.model;

public class User {
    private String username;
    private String realName;
    private byte age;
    private String location;

    public User(String username, String realName, byte age, String location) {
        this.username = username;
        this.realName = realName;
        this.age = age;
        this.location = location;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
