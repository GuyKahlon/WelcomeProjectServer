package com.citi.innovaciti.welcome.domain;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Liron on 27/04/2014.
 */
@Entity
@Table(name = "hosts",
        indexes = {
                @Index(name = "host_phoneNumber_idx", columnList = "phoneNumber", unique = true),
                @Index(name = "host_active_idx", columnList = "active")
        })
public class Host {

    @Id
    @GeneratedValue
    private long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String picUrl;

    private String email;

    @JsonIgnore
    private boolean active = true; //initialized to true


    public Host() {
    }

    public Host(String firstName, String lastName, String phoneNumber, String picUrl, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.picUrl = picUrl;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Host{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                '}';
    }
}
