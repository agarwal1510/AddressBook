package com.agarwalaman.addressbook.dao;

import com.agarwalaman.addressbook.entity.Contact;
import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.List;

public class ContactDao {

    private Client client;
    private Gson gson;

    public ContactDao(Client client, Gson gson) {
        this.client = client;
        this.gson = gson;
    }

    public boolean addContact(Contact contact) {
        IndexResponse response = client.prepareIndex("addressbook", "contact")
                .setSource(gson.toJson(contact), XContentType.JSON).get();
        System.out.print(response.getId() + " " + response.getIndex() + " " + response.getType());
        return false;
    }

    public Contact getContact(String name) {
        return null;
    }

    public List<Contact> getContacts() {
        return null;
    }

    public boolean updateContact(String name, Contact contact) {
        return false;
    }

    public boolean removeContact(String name) {
        return false;
    }

}
