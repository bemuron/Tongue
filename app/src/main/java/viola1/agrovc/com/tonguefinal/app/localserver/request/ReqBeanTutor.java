package viola1.agrovc.com.tonguefinal.app.localserver.request;

import android.widget.ImageView;

public class ReqBeanTutor {

    private String Firstname;


    private String Lastname;
    private String description;
    private String Contact;

    private int salary;

    private int years;

    private String Gender;
    private String Email;
    private String Country;
    private String Date;

private ImageView profile_pic;

    private String level;
    private String NIN;
    private String district;
    private String address;
    private String language_spoken;
    private String language_taught;
    private String next;
    private String nextcontact;
    private String nextrelationship;
    private String nextaddress;







    private String request_type;
    private String user_type;

    public ReqBeanTutor() {
        this.request_type = "api";

    }

    public String getNextaddress() {
        return nextaddress;
    }

    public void setNextaddress(String nextaddress) {
        this.nextaddress= nextaddress;
    }
    public String getNextcontact() {
        return nextcontact;
    }

    public void setNextcontact(String nextcontact) {
        this.nextcontact= nextcontact;
    }

    public String getNextrelationship() {
        return nextrelationship;
    }

    public void setNextrelationship(String nextrelationship) {
        this.nextrelationship= nextrelationship;
    }



    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next= next;
    }

    public String getLanguage_spoken() {
        return language_spoken;
    }

    public void setLanguage_spoken(String language_spoken) {
        this.language_spoken=language_spoken;
    }

    public String getLanguage_taught() {
        return language_taught;
    }

    public void setLanguage_taught(String language_taught) {
        this.language_taught=language_taught;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address= address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district= district;
    }





    public String getNIN() {
        return NIN;
    }

    public void setNIN(String NIN) {
        this.NIN=NIN;
    }
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level= level;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String Firstname) {
        this.Firstname= Firstname;
    }


    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String Contact) {
        this.Contact = Contact;
    }



    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String Lastname) {
        this.Lastname = Lastname;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public int getsalary() {
        return salary;
    }

    public void setsalary(int salary) {
        this.salary = salary;
    }

    public int getyears() {
        return years;
    }

    public void setyears(int years) {
        this.years = years;
    }


    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

}

