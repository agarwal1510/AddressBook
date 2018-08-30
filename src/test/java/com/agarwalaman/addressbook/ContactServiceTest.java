package com.agarwalaman.addressbook;

import com.agarwalaman.addressbook.dao.ContactDao;
import com.agarwalaman.addressbook.entity.Contact;
import com.agarwalaman.addressbook.service.ContactService;
import com.agarwalaman.addressbook.service.ContactServiceImplementation;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import spark.Spark;

public class ContactServiceTest {

    private static ContactDao mockedContactDao;
    private static ContactService contactService;

    @Before
    public void setUp() {
        mockedContactDao = Mockito.mock(ContactDao.class);
        contactService = new ContactServiceImplementation(mockedContactDao);
    }

    @Test
    public void shouldTestAddingContact() {
//        Contact contact = new Contact("Aman", "631-820-5346");
//        Mockito.when(mockedContactDao.addContact(contact))

    }

    @Test
    public void shouldTestGettingContact() {
        Contact contact = new Contact("Aman", "631-820-5346");
        Mockito.when(mockedContactDao.getContact("Aman")).thenReturn(contact);
        Assert.assertEquals(contactService.getContact("Aman"), contact);
        Mockito.verify(mockedContactDao).getContact("Aman");
    }

    @Test
    public void shouldTestRemovingContact() {

    }
}
