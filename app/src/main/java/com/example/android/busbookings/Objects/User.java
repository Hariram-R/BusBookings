package com.example.android.busbookings.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    String first_name,last_name,age,passwword;
    @PrimaryKey
    String email;

    public User(String first_name, String last_name, String age,String passwword, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.passwword = passwword;
        this.email = email;
    }

    public User() {
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }


    public String getPasswword() {
        return passwword;
    }

    public void setPasswword(String passwword) {
        this.passwword = passwword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
