package app.com.thetechnocafe.eventos.Models;

/**
 * Created by gurleensethi on 25/11/16.
 */

public class CommentsModel {
    private String mEventID;
    private String mComment;
    private long mTime;
    private String mFrom;

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public String getEventID() {
        return mEventID;
    }

    public void setEventID(String eventID) {
        mEventID = eventID;
    }

    public String getFrom() {
        return mFrom;
    }

    public void setFrom(String from) {
        mFrom = from;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }
}
