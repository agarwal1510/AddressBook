package com.agarwalaman.addressbook;

import com.agarwalaman.addressbook.dao.ContactDao;
import com.agarwalaman.addressbook.entity.Contact;
import com.agarwalaman.addressbook.service.ContactService;
import com.agarwalaman.addressbook.service.ContactServiceImplementation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the contact service class and business logic
 */
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
        Contact contact = new Contact("Aman", "631-820-5346");
        Mockito.when(mockedContactDao.addContact(contact)).thenReturn(true);
        Assert.assertEquals(contactService.addContact(contact), true);
        Mockito.verify(mockedContactDao).addContact(contact);
    }

    @Test
    public void shouldTestFetchingContact() {
        Contact contact = new Contact("Aman", "631-820-5346");
        Mockito.when(mockedContactDao.getContact("Aman")).thenReturn(contact);
        Assert.assertEquals(contactService.getContact("Aman"), contact);
        Mockito.verify(mockedContactDao).getContact("Aman");
    }

    @Test
    public void shouldTestRemovingContact() {
        Mockito.when(mockedContactDao.removeContact("Aman")).thenReturn(true);
        Assert.assertEquals(contactService.removeContact("Aman"), true);
        Mockito.verify(mockedContactDao).removeContact("Aman");
    }

    @Test
    public void shouldTestUpdatingContact() {
        Contact contact = new Contact("Aman", "631-820-5347");
        Mockito.when(mockedContactDao.updateContact("Aman", contact)).thenReturn(true);
        Assert.assertEquals(contactService.updateContact("Aman", contact), true);
        Mockito.verify(mockedContactDao).updateContact("Aman", contact);
    }

    @Test
    public void shouldTestFetchingListOfContacts() {
        List<Contact> contactList = new ArrayList<>();
        Contact contact1 = new Contact("Aman", "631-820-5346");
        Contact contact2 = new Contact("Melody", "631-820-7890");
        Contact contact3 = new Contact("Tim", "631-820-1234");
        contactList.add(contact1);
        contactList.add(contact2);
        contactList.add(contact3);

        Mockito.when(mockedContactDao.getContacts(3, 1, "")).thenReturn(contactList);
        Assert.assertEquals(contactService.getContacts(3, 1, ""), contactList);
        Mockito.verify(mockedContactDao).getContacts(3, 1, "");
    }

    @Test
    public void shouldTestPhoneNumberValidation() {
        Contact contact = new Contact("Aman", "631-820-53467");
        Assert.assertEquals(contactService.addContact(contact), false);

        contact = new Contact("Aman", "123456789012");
        Assert.assertEquals(contactService.addContact(contact), false);

        contact = new Contact("Aman", "12345678");
        Assert.assertEquals(contactService.addContact(contact), false);

        contact = new Contact("Aman", "631-820-5346");
        Assert.assertEquals(contactService.addContact(contact), true);

        contact = new Contact("Aman", "+1 631-820-5346");
        Assert.assertEquals(contactService.addContact(contact), true);

        contact = new Contact("Aman", "(631) 820-5346");
        Assert.assertEquals(contactService.addContact(contact), true);
    }


}
