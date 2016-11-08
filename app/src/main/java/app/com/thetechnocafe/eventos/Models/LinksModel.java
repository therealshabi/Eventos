package app.com.thetechnocafe.eventos.Models;

/**
 * Created by gurleensethi on 08/11/16.
 */

public class LinksModel {
    private String linkName;
    private String linkAddress;
    private String eventID;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }
}
