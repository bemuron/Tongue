package viola1.agrovc.com.tonguefinal.app.localserver.request;

import viola1.agrovc.com.tonguefinal.constants.AppProperties;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */



public class ReqBeanApp {
    private String request_type = AppProperties.SERVER_REQUEST_TYPE;
    private String member_email;

    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

}

