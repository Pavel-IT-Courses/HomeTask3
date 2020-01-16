package com.gmail.pavkascool.hometask3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Person {



    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private String contact;
    private boolean hasEmail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isHasEmail() {
        return hasEmail;
    }

    public void setHasEmail(boolean hasEmail) {
        this.hasEmail = hasEmail;
    }
}

