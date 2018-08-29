package viola1.agrovc.com.tonguefinal.app.localserver.request.response;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


public class ResBeanSignup {

    private String response_status;
    private String error;
    private String response_description;

    private String registration_status;

    public ResBeanSignup() {
    }

    public String getResponse_status() {
        return response_status;
    }

    public String getError() {
        return error;
    }

    public String getResponse_description() {
        return response_description;
    }

    public String getRegistration_status() {
        return registration_status;
    }
}

