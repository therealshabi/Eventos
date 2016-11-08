package app.com.thetechnocafe.eventos.Models;

/**
 * Created by gurleensethi on 08/11/16.
 */

public class ContactsModel {
    private String eventsID;
    private String contactName;
    private String emailID;
    private String phoneNumber;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEventsID() {
        return eventsID;
    }

    public void setEventsID(String eventsID) {
        this.eventsID = eventsID;
    }
}
