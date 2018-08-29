package com.agarwalaman.addressbook.response;

import com.agarwalaman.addressbook.entity.Contact;

import java.util.List;

public class Response {

    private ResponseStatus status;
    private String message;
    private Contact contact;
    private List<Contact> contactList;

    public Response(ResponseStatus status, Exception e) {
        this.status = status;
        this.message = e.getMessage();
    }

    public Response(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(ResponseStatus status, Contact contact) {
        this.status = status;
        this.contact = contact;
    }

    public Response(ResponseStatus status, List<Contact> list) {
        this.status = status;
        this.contactList = list;
    }

    public Response(ResponseStatus status, String message, String... args) {
        this.status = status;
        this.message = String.format(message, args);
    }
}
