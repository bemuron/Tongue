package viola1.agrovc.com.tonguefinal.data.network;

import com.google.gson.annotations.SerializedName;

import viola1.agrovc.com.tonguefinal.models.User;

public class Result {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    public Result(Boolean error, String message, User user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }


    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

}
