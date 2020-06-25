package com.pramod.apartmentrental.Admin.users;

public class Users {

    private String name;
    private String email;
    private String phone;
    private String photo;
    private String id;
    private String role;

    public Users(String id, String name, String email, String phone, String photo, String role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
        this.id = id;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
