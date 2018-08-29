package viola1.agrovc.com.tonguefinal.app.localserver.request;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


public class ReqBeanSignup {

    private String email;


    private String password;

    private String request_type;
    private String user_type;

    public ReqBeanSignup() {
        this.request_type = "api";

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

}

