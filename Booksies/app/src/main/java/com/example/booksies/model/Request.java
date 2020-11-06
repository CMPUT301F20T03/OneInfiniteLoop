package com.example.booksies.model;

public class Request {

    private String owner;
    private String borrower;
    private boolean notified;
    private String status;

    public Request(String owner, String borrower, String status) {
        this.owner = owner;
        this.borrower = borrower;
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
