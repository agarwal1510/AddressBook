package com.agarwalaman.addressbook.response;

public enum ResponseStatus {

    SUCCESS("Success"), FAILURE("Failure");

    private String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
