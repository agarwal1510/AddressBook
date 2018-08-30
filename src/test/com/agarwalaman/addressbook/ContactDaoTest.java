package com.agarwalaman.addressbook;

import com.agarwalaman.addressbook.dao.ContactDao;
import com.agarwalaman.addressbook.elasticsearch.ElasticSearchClient;
import com.agarwalaman.addressbook.entity.Contact;
import com.google.gson.Gson;
import org.elasticsearch.client.Client;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Integration test for elastic search. Order of execution has to be maintained to correctly check the system.
 */
@RunWith(com.carrotsearch.randomizedtesting.RandomizedRunner.class)
public class ContactDaoTest {

    private Client client;
    private Gson gson;
    private ContactDao contactDao;

    @Before
    public void setUp() {
        client = ElasticSearchClient.client();
        gson = new Gson();
        contactDao = new ContactDao(client, gson);
    }

    @Test
    public void shouldTestAddContact() {
        Contact contact = new Contact("Tom", "631-820-5347");
        if (contactDao.addContact(contact)) {
            Contact temp = contactDao.getContact("Tom");
            Assert.assertEquals(temp, contact);
        }

        Contact contact2 = new Contact("Jeremy", "631-820-5347");
        if (contactDao.addContact(contact2)) {
            Contact temp = contactDao.getContact("Jeremy");
            Assert.assertEquals(temp, contact2);
        }

        Contact contact3 = new Contact("Ryan", "631-820-5347");
        if (contactDao.addContact(contact3)) {
            Contact temp = contactDao.getContact("Ryan");
            Assert.assertEquals(temp, contact3);
        }
    }

    @Test
    public void shouldTestGetContact() {
        Contact temp2 = contactDao.getContact("Gibriel");
        Assert.assertEquals(temp2, null);
    }

    @Test
    public void shouldTestUpdateContact() {
        Contact contact = new Contact("Jeremy", "631-820-5656");
        if (contactDao.updateContact("Jeremy", contact)) {
            Contact updated = contactDao.getContact("Jeremy");
            Assert.assertEquals(updated, contact);
        }

    }

    @Test
    public void shouldTestRemovingContact() {
        if (contactDao.removeContact("Ryan")) {
            Assert.assertEquals(contactDao.getContact("Ryan"), null);
        }
    }

    @After
    public void destroy() {
        client.close();
    }
}
