package viola1.agrovc.com.tonguefinal.app.localserver;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


public class UserBioData {
    private String firstName;
    private String lastName;
    private String contact;
    private String sex;
    private String regDate;
    private String userName;
    private String businessName;
    private String email;
    private String role;
    private String status;

    public UserBioData() {
    }

    public UserBioData(String firstName, String lastName, String contact, String sex, String regDate, String userName, String businessName, String email, String role, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.sex = sex;
        this.regDate = regDate;
        this.userName = userName;
        this.businessName = businessName;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

