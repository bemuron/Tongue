package viola1.agrovc.com.tonguefinal.presentation.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.database.Cursor;
import android.util.Log;

import java.util.HashMap;

import viola1.agrovc.com.tonguefinal.data.TongueRepository;
import viola1.agrovc.com.tonguefinal.models.User;

import static android.support.constraint.Constraints.TAG;

public class LoginRegisterActivityViewModel extends ViewModel {
    //private member variable to hold reference to the repository
    private TongueRepository mRepository;

    private HashMap<String, String> mUser;

    private Cursor userDetailsCursor;

    //constructor that gets a reference to the repository and gets the categories
    public LoginRegisterActivityViewModel(TongueRepository repository) {
        //super(application);
        mRepository = repository;
    }

    public HashMap<String, String> getUserDetails() {
        mUser = new HashMap<String, String>();

        userDetailsCursor = mRepository.getUser();
        // Move to first row
        userDetailsCursor.moveToFirst();
        if (userDetailsCursor.getCount() > 0) {
            mUser.put("name", userDetailsCursor.getString(1));
            mUser.put("email", userDetailsCursor.getString(2));
            mUser.put("uid", userDetailsCursor.getString(3));
            mUser.put("created_at", userDetailsCursor.getString(4));
        }
        userDetailsCursor.close();
        // return user
        Log.d(TAG, "Got user from db: " + mUser.toString());

        return mUser;
    }

    //call repository method to handle posting data to server
    public void loginUser(String email, String password){
        mRepository.loginFixAppUser(email, password);
    }

    //call repository method to handle posting user reg details to server
    public void registerUser(String name, String date_of_birth, String gender, String email, String password){
        mRepository.registerFixAppUser(name, date_of_birth, gender, email, password);
    }

    public void insert(User user) { mRepository.insertUser(user); }

    public void delete() { mRepository.deleteUser();}
}
