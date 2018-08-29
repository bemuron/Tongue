package viola1.agrovc.com.tonguefinal.app.localserver.request.response;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


public class ResBeanUserProfile {

    private String response_status;
    private String error;
    private String response_description;

    private String member_firstname;
    private String member_lastname;
    private String member_contact;
    private String member_sex;
    private String reg_date;
    private String member_username;
    private String business_name;
    private String member_email;
    private String role;
    private String status;

    public String getMember_firstname() {
        return member_firstname;
    }

    public String getMember_lastname() {
        return member_lastname;
    }

    public String getMember_contact() {
        return member_contact;
    }

    public String getMember_sex() {
        return member_sex;
    }

    public String getReg_date() {
        return reg_date;
    }

    public String getMember_username() {
        return member_username;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public String getMember_email() {
        return member_email;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
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
}

