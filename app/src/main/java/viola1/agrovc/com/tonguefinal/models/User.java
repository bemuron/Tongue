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

    @ColumnInfo(name = KEY_GENDER)
    private String gender;

    @ColumnInfo(name = KEY_DOB)
    private String date_of_birth;

    @ColumnInfo(name = KEY_PASSWORD)
    private String password;

    @ColumnInfo(name = KEY_CREATED_ON)
    private String created_on;

    @Ignore
    public User(String name, String date_of_birth, String gender,
                String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.created_on = created_on;
    }

    @Ignore
    public User(int user_id, String name, String email, String gender,
                String date_of_birth, String created_on){
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.created_on = created_on;
    }

    public User(int user_id, String name, String email, String password,
                String gender, String date_of_birth, String created_on) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.created_on = created_on;
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
}
