package viola1.agrovc.com.tonguefinal.presentation.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;

import viola1.agrovc.com.tonguefinal.data.TongueRepository;
import viola1.agrovc.com.tonguefinal.data.database.Language;

public class HomeActivityViewModel extends ViewModel {

    //private member variable to hold reference to the repository
    private TongueRepository mRepository;

    //private LiveData member variable to cache the categories
    private LiveData<List<Language>> mAllLanguages;

    //constructor that gets a reference to the repository and gets the languages
    public HomeActivityViewModel(TongueRepository repository) {
        mRepository = repository;
        mAllLanguages = mRepository.getAllLanguages();
    }

    //a getter method for all the categories. This hides the implementation from the UI
    public LiveData<List<Language>> getAllLanguages(){
        return mAllLanguages;
    }

    //a wrapper insert() method that calls the Repository's insert() method. In this way,
    // the implementation of insert() is completely hidden from the UI.
    //public void insert(Category category) { mRepository.insert(category); }


}
