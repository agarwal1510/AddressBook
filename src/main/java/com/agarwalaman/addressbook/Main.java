package com.agarwalaman.addressbook;

import com.agarwalaman.addressbook.controller.ContactController;
import com.agarwalaman.addressbook.dao.ContactDao;
import com.agarwalaman.addressbook.elasticsearch.ElasticSearchClient;
import com.agarwalaman.addressbook.service.ContactService;
import com.agarwalaman.addressbook.service.ContactServiceImplementation;
import com.google.gson.Gson;


/**
 * Application/Driver class
 */
public class Main {

    private static ContactService service;
    private static ContactDao contactDao;
    private static Gson gson;

    public static void main(String[] args) {
        gson = new Gson();
        contactDao = new ContactDao(ElasticSearchClient.client(), gson);
        service = new ContactServiceImplementation(contactDao);
        ContactController.handleRequests(service, gson);
    }
}
