package viola1.agrovc.com.tonguefinal.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

import viola1.agrovc.com.tonguefinal.models.User;


/**
 * Created by BE on 2/3/2018.
 */

@Dao
public interface UsersDao {

    /*
    insert user into db
     */
    @Insert
    void insertUser(User user);

    @Query("Delete from user")
    void deleteUser();

    @Query("SELECT * from user")
    //HashMap<String, String> getUserDetails();
    //Cursor getUserDetails();
    LiveData<List<User>> getUserDetails();

    //update user details
    @Query("UPDATE user SET name = :name, description = :description, " +
            "date_of_birth = :date_of_birth, gender = :gender WHERE user_id = :user_id")
    void updateProfile(String name, String description, String date_of_birth, String gender, int user_id);
}
