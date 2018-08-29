package com.agarwalaman.addressbook.service;

import com.agarwalaman.addressbook.dao.ContactDao;
import com.agarwalaman.addressbook.entity.Contact;

import java.util.List;
import java.util.regex.Pattern;

public class ContactServiceImplementation implements ContactService {

    private ContactDao contactDao;

    public ContactServiceImplementation(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public boolean addContact(Contact contact) {
        if (!validatePhoneNumber(contact.getNumber())) {
            System.out.print("Invalid Number: " + contact.getNumber());
            return false;
        } else {
            System.out.print("Dao called");
            contactDao.addContact(contact);
            return true;
        }
    }

    public Contact getContact(String name) {
        return contactDao.getContact(name);
    }

    @Override
    public List<Contact> getContacts(int pageSize, int page, String query) {

        return null;
    }

    public boolean updateContact(String name, Contact contact) {
        return contactDao.updateContact(name, contact);
    }

    public boolean removeContact(String name) {
        return contactDao.removeContact(name);
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("^[0-9]{10}$")) {
            return true;
        } else {
            return false;
        }

    }
}
