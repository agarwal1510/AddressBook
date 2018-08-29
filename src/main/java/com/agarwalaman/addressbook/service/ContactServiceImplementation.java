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
        if (pageSize < 0 || page <= 0) {
            return null;
        }
        return contactDao.getContacts(pageSize, page, query);
    }

    public boolean updateContact(String name, Contact contact) {
        return contactDao.updateContact(name, contact);
    }

    public boolean removeContact(String name) {
        return contactDao.removeContact(name);
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        String regex = "^\\+?1?[-.\\s]?\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
        Pattern pattern = Pattern.compile(regex);
        if (pattern.matcher(phoneNumber).matches()) {
            return true;
        } else {
            return false;
        }

    }
}
