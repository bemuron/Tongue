package viola1.agrovc.com.tonguefinal.app.localserver.request;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


public class ReqBeanLogin {
    private String email;
    private String password;
    private String type;
    private String _token;

    public ReqBeanLogin() {
    }

    public String get_token() {
        return _token;
    }

    public void set_token(String _token) {
        this._token = _token;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
