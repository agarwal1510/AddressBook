package com.agarwalaman.addressbook.controller;

import com.agarwalaman.addressbook.entity.Contact;
import com.agarwalaman.addressbook.response.Response;
import com.agarwalaman.addressbook.response.ResponseStatus;
import com.agarwalaman.addressbook.service.ContactService;
import com.google.gson.Gson;

import java.util.List;

import static spark.Spark.*;

/**
 * Controller class which handles all the incoming HTTP requests
 * and routes the request to the service for appropriate business logic to be executed
 * and returns the JSON response
 */
public class ContactController {

    public static void handleRequests(final ContactService service, final Gson gson) {

        get("/", (request, response) -> {
            return "Welcome to Address Book";
        });

        /**
         *  POST request handler to add contact
         */
        post("/contact", (request, response) -> {
            Contact contact = gson.fromJson(request.body(), Contact.class);
            response.type("application/json");
            if (service.addContact(contact)) {
                return gson.toJson(new Response(ResponseStatus.SUCCESS, "Contact added successfully"));
            }
            response.status(404);
            return gson.toJson(new Response(ResponseStatus.FAILURE, "Something went wrong while adding the contact, check the number and try again!"));
        });

        /**
         *  GET request handler to fetch contact by name
         */
        get("/contact/:name", (request, response) -> {
            String name = request.params(":name");
            Contact contact = service.getContact(name);
            response.type("application/json");
            if (contact != null) {
                return gson.toJson(new Response(ResponseStatus.SUCCESS, contact));
            }
            response.status(404);
            return gson.toJson(new Response(ResponseStatus.FAILURE, "Contact with name: %s not found, try adding the contact before you search", name));
        });

        /**
         *  PUT request handler for updating contact by name
         */
        put("/contact/:name", (request, response) -> {
            response.type("application/json");
            String name = request.params(":name");
            Contact contact = gson.fromJson(request.body(), Contact.class);
            if (service.updateContact(name, contact)) {
                return gson.toJson(new Response(ResponseStatus.SUCCESS, "Contact updated successfully"));
            }
            response.status(404);
            return gson.toJson(new Response(ResponseStatus.FAILURE, "Contact with name: %s not found", name));
        });

        /**
         *  DELETE request handler to delete a contact by name
         */
        delete("contact/:name", (request, response) -> {
            response.type("application/json");
            String name = request.params(":name");
            if (service.removeContact(name)) {
                return gson.toJson(new Response(ResponseStatus.SUCCESS, "Contact deleted successfully"));
            }
            response.status(404);
            return gson.toJson(new Response(ResponseStatus.FAILURE, "Contact with name: %s not found", name));
        });

        /**
         *  GET request handler to return a list of contacts based on query string params
         */
        get("/contact", (request, response) -> {
            String pageSize = request.queryParams("pageSize");
            String page = request.queryParams("page");
            String query = request.queryParams("query");
            response.type("application/json");
            List<Contact> list = service.getContacts(Integer.valueOf(pageSize), Integer.valueOf(page), query);
            if (list != null && list.size() > 0) {
                return gson.toJson(new Response(ResponseStatus.SUCCESS, list));
            }
            return gson.toJson(new Response(ResponseStatus.FAILURE, "No more entries found, try changing the offset"));
        });
    }
}
