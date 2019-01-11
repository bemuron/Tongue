package viola1.agrovc.com.tonguefinal.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")
public final class User {

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DOB = "date_of_birth";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "user_id";
    private static final String KEY_ROLE = "role";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PROFILE_PIC = "profile_pic";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CREATED_ON = "created_on";

    @PrimaryKey
    @ColumnInfo(name = KEY_UID)
    private int user_id;

    //@NonNull
    @ColumnInfo(name = KEY_NAME)
    private String name;

    @ColumnInfo(name = KEY_EMAIL)
    private String email;

    @ColumnInfo(name = KEY_ROLE)
    private String role;

    @ColumnInfo(name = KEY_DESCRIPTION)
    private String description;

    @ColumnInfo(name = KEY_PROFILE_PIC)
    private String profile_pic;

    @ColumnInfo(name = KEY_PHONE_NUMBER)
    private String phone_number;

    @ColumnInfo(name = KEY_GENDER)
    private String gender;

    @ColumnInfo(name = KEY_DOB)
    private String date_of_birth;

    @ColumnInfo(name = KEY_PASSWORD)
    private String password;

    @ColumnInfo(name = KEY_CREATED_ON)
    private String created_on;

    /*@Ignore
    public User(String name, String date_of_birth, String gender,
                String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
    }*/

    /*@Ignore
    public User(int user_id, String name, String email, String gender,
                String date_of_birth, String created_on){
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.created_on = created_on;
    }*/

    /*public User(int user_id, String name, String email, String password,
                String gender, String date_of_birth, String created_on) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.created_on = created_on;
    }*/

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getCreated_on() {
        return created_on;
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
