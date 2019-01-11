package viola1.agrovc.com.tonguefinal.models;

import java.io.Serializable;

public class NotificationMessage implements Serializable {
    String id, message, createdAt, meeting_date, meeting_time, meeting_location;
    int meeting_id;
    NotificationUser notificationUser;

    public NotificationMessage() {
    }

    public NotificationMessage(String id, String message, String createdAt, NotificationUser notificationUser) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.notificationUser = notificationUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public NotificationUser getNotificationUser() {
        return notificationUser;
    }

    public void setNotificationUser(NotificationUser notificationUser) {
        this.notificationUser = notificationUser;
    }

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getMeeting_date() {
        return meeting_date;
    }

    public void setMeeting_date(String meeting_date) {
        this.meeting_date = meeting_date;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

    public void setMeeting_time(String meeting_time) {
        this.meeting_time = meeting_time;
    }

    public String getMeeting_location() {
        return meeting_location;
    }

    public void setMeeting_location(String meeting_location) {
        this.meeting_location = meeting_location;
    }
}
