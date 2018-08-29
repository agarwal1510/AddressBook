package com.agarwalaman.addressbook.response;

import com.agarwalaman.addressbook.entity.Contact;

public class Response {

    private ResponseStatus status;
    private String message;
    private Contact contact;

    public Response(Exception e) {
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

    public Response(ResponseStatus status, String message, String... args) {
        this.status = status;
        this.message = String.format(message, args);
    }

    public String getMessage() {
        return message;
    }
}
