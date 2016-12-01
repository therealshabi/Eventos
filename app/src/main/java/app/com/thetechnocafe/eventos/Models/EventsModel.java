package app.com.thetechnocafe.eventos.Models;

import java.util.Date;

/**
 * Created by gurleensethi on 15/10/16.
 */

public class EventsModel {

    private String id;
    private String title;
    private String description;
    private String venue;
    private Date date;
    private int avatar_id;
    private String image;
    private String requirements;
    private String submittedBy;
    private boolean verified;
    private int numOfPeopleInterested;
    private boolean outside_event;

    public int getAvatarId() {
        return avatar_id;
    }

    public void setAvatarId(int avatar_id) {
        this.avatar_id = avatar_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public int getAvatar_id() {
        return avatar_id;
    }

    public void setAvatar_id(int avatar_id) {
        this.avatar_id = avatar_id;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getNumOfPeopleInterested() {
        return numOfPeopleInterested;
    }

    public void setNumOfPeopleInterested(int numOfPeopleInterested) {
        this.numOfPeopleInterested = numOfPeopleInterested;
    }

    public boolean getOutsideEvent() {
        return outside_event;
    }

    public void setOutsideEvent(boolean outside_event) {
        this.outside_event = outside_event;
    }
}
