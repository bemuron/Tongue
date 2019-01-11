package viola1.agrovc.com.tonguefinal.presentation.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import viola1.agrovc.com.tonguefinal.data.TongueRepository;
import viola1.agrovc.com.tonguefinal.data.database.Language;
import viola1.agrovc.com.tonguefinal.models.User;

public class UserProfileActivityViewModel extends ViewModel {

    //private member variable to hold reference to the repository
    private TongueRepository mRepository;

    //private LiveData member variable to cache the user profile details
    private LiveData<List<User>> mUserDetails;

    //constructor that gets a reference to the repository and gets the languages
    public UserProfileActivityViewModel(TongueRepository repository) {
        mRepository = repository;
        mUserDetails = mRepository.getUserDetails();
    }

    //a getter method for all the user details. This hides the implementation from the UI
    public LiveData<List<User>> getUserDetails(){
        return mUserDetails;
    }

    //a wrapper insert() method that calls the Repository's insert() method. In this way,
    // the implementation of insert() is completely hidden from the UI.
    //public void insert(Category category) { mRepository.insert(category); }

    //deletes user from sqlite db
    public void delete() { mRepository.deleteUser();}

    //updates user profile in room db
    public void updateProfile(String name, String description,
                              String date_of_birth, String gender, int user_id){
        mRepository.updateProfile(name, description, date_of_birth, gender, user_id);
    }


}
