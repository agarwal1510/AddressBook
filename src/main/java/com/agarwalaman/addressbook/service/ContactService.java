package com.agarwalaman.addressbook.service;

import com.agarwalaman.addressbook.entity.Contact;

import java.util.List;

/**
 * Defines the contract for the contact service
 */
public interface ContactService {

    boolean addContact(Contact contact);

    Contact getContact(String name);

    List<Contact> getContacts(int pageSize, int page, String query);

    boolean updateContact(String name, Contact contact);

    boolean removeContact(String name);

}