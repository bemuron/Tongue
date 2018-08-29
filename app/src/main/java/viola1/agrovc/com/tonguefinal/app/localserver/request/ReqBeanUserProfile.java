package viola1.agrovc.com.tonguefinal.app.localserver.request;

import viola1.agrovc.com.tonguefinal.constants.AppProperties;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */



public class ReqBeanUserProfile {
    private String member_email;
    private String request_type;

    public ReqBeanUserProfile() {
        this.request_type = AppProperties.SERVER_REQUEST_TYPE;
    }

    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }
}

