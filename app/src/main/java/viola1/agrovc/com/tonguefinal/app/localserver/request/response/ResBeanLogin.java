package viola1.agrovc.com.tonguefinal.app.localserver.request.response;


import viola1.agrovc.com.tonguefinal.app.localserver.UserLocation;
import viola1.agrovc.com.tonguefinal.app.localserver.UserPaymentAccount;

public class ResBeanLogin {
    private String response_status;
    private String error;

    private String login_status;

    private ResBeanUserProfile profile_details;
    private UserLocation mandatory_location;
    private UserLocation optional_location;
    private UserPaymentAccount payment_accounts;

    public ResBeanLogin() {
    }

    public ResBeanUserProfile getProfile_details() {
        return profile_details;
    }

    public String getResponse_status() {
        return response_status;
    }

    public String getError() {
        return error;
    }

    public String getLogin_status() {
        return login_status;
    }

    public UserLocation getMandatory_location() {
        return mandatory_location;
    }

    public UserLocation getOptional_location() {
        return optional_location;
    }

    public UserPaymentAccount getPayment_accounts() {
        return payment_accounts;
    }
}

