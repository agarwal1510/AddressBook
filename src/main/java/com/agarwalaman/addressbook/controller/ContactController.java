package com.agarwalaman.addressbook.controller;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.agarwalaman.addressbook.service.ContactService;
import com.agarwalaman.addressbook.entity.Contact;

import java.util.List;

public class ContactController {

    public static void handleRequests(final ContactService service, final Gson gson) {

        get("/", (request, response) -> {
            System.out.println("inside welcome");
            return "Welcome to Address Book";
        });

        post("/contact", (request, response) -> {
            Contact contact = gson.fromJson(request.body(), Contact.class);
            System.out.println("inside post: " + request.body());
            response.type("application/json");
            if (service.addContact(contact)) {
                return "Success";
            }
            return "Failure";
        });

        get("/contact/:name", (request, response) -> {
            Contact contact = service.getContact(request.params(":name"));
            response.type("application/json");
            if (contact != null) {
                return gson.toJson(contact, Contact.class);
            }
            response.status(400);
            return "Failure";
        });

        put("/contact/:name", (request, response) -> {
            response.type("application/json");
            Contact contact = gson.fromJson(request.body(), Contact.class);
            if (service.updateContact(request.params(":name"), contact)) {
                return "Success";
            }
            return "Failure";
        });

        delete("contact/:name", (request, response) -> {
            response.type("application/json");
            if (service.removeContact(request.params(":name"))) {
                return "Success";
            }
            return "Failure";
        });

        get("/contact", (request, response) -> {
            String pageSize = request.queryParams("pageSize");
            String page = request.queryParams("page");
            String query = request.queryParams("query");

            List<Contact> list = service.getContacts(Integer.valueOf(pageSize), Integer.valueOf(page), query);
            response.type("application/json");
            return gson.toJson(list);
        });
    }
}
