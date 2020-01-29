package com.gmail.pavkascool.hw10_content_resolver;

public class Contact {
    private String name;
    private String contactData;
    private long id;

    public Contact(long id, String name, String contactData) {
        this.id = id;
        this.name = name;
        this.contactData = contactData;
    }

    public String getName() {
        return name;
    }

    public String getContactData() {
        return contactData;
    }

    public long getId() {
        return id;
    }
}
