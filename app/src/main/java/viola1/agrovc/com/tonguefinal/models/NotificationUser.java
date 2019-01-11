package viola1.agrovc.com.tonguefinal.models;

import java.io.Serializable;

public class NotificationUser implements Serializable {
    String name, email;
    int id;

    public NotificationUser() {
    }

    public NotificationUser(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
