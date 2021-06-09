package com.isi.myapplication.entities;

public class User {
    private int id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private String level;
    private String password;
    private String password2nd;

    public User() {
    }

    public User(int id, String email, String firstname, String lastname, String phone, String level, String password) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.level = level;
        this.password = password;
    }

    public User(int id, String email, String firstname, String lastname, String phone, String level, String password, String password2nd) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.level = level;
        this.password = password;
        this.password2nd = password2nd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2nd() {
        return password2nd;
    }

    public void setPassword2nd(String password2nd) {
        this.password2nd = password2nd;
    }
}
