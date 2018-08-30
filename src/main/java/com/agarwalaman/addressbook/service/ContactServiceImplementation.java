package com.agarwalaman.addressbook.service;

import com.agarwalaman.addressbook.dao.ContactDao;
import com.agarwalaman.addressbook.entity.Contact;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Binds the DAO class and the controller, provides all the action methods and contains business logic
 */
public class ContactServiceImplementation implements ContactService {

    private ContactDao contactDao;

    public ContactServiceImplementation(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    /**
     * Adds a contact to the elastic search index
     *
     * @param contact
     * @return true if added successfully else false
     */
    public boolean addContact(Contact contact) {
        if (!validatePhoneNumber(contact.getNumber())) {
            return false;
        } else {
            contactDao.addContact(contact);
            return true;
        }
    }

    /**
     * Fetches the contact given a contact name
     *
     * @param name
     * @return Contact object if found else null
     */
    public Contact getContact(String name) {
        return contactDao.getContact(name);
    }

    /**
     * Fetches a list of contacts based on following params
     *
     * @param pageSize the number of contacts to be returned
     * @param page     indicates the page offset
     * @param query    represents the Elastic Search queryStringQuery
     * @return List of contacts
     */
    public List<Contact> getContacts(int pageSize, int page, String query) {
        if (pageSize < 0 || page <= 0) {
            return null;
        }
        return contactDao.getContacts(pageSize, page, query);
    }

    /**
     * Updates an existing contact given a name
     *
     * @param name
     * @param contact Updated contact
     * @return true if found and successfully updated else false
     */
    public boolean updateContact(String name, Contact contact) {
        return contactDao.updateContact(name, contact);
    }

    /**
     * Removes a contact from the data store given a name
     *
     * @param name
     * @return true if successfully removed else false
     */
    public boolean removeContact(String name) {
        return contactDao.removeContact(name);
    }

    /**
     * Validates a US phone number
     *
     * @param phoneNumber
     * @return true if valid else false
     */
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
