package io.payex.android;

/**
 * Created by vince on 11/28/2016.
 */

public class Bank {
    private String bin;
    private String name;
    private String email;
    private String contact1;
    private String contact2;

    public Bank(String bin, String name, String email, String contact1, String contact2) {
        this.bin = bin;
        this.name = name;
        this.email = email;
        this.contact1 = contact1;
        this.contact2 = contact2;
    }

    public String getBin() {
        return bin;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact1() {
        return contact1;
    }

    public String getContact2() {
        return contact2;
    }
}
