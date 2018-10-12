package viola1.agrovc.com.tonguefinal.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import java.util.List;

import viola1.agrovc.com.tonguefinal.AppExecutors;
import viola1.agrovc.com.tonguefinal.data.database.Language;
import viola1.agrovc.com.tonguefinal.data.database.LanguagesDao;
import viola1.agrovc.com.tonguefinal.data.database.TongueDatabase;
import viola1.agrovc.com.tonguefinal.data.database.UsersDao;
import viola1.agrovc.com.tonguefinal.data.network.FetchLanguages;
import viola1.agrovc.com.tonguefinal.data.network.LoginUser;
import viola1.agrovc.com.tonguefinal.data.network.RegisterUser;
import viola1.agrovc.com.tonguefinal.models.User;
import viola1.agrovc.com.tonguefinal.utilities.InjectorUtils;

/**
 * the TongueRepository is a singleton.
 * Handles data operations in Tongue. Acts as a mediator between {@link }
 * and {@link }
 */

public class TongueRepository{
    private static final String LOG_TAG = TongueRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static TongueRepository sInstance;
    private LanguagesDao mLanguagesDao;
    private AppExecutors mExecutors;
    private FetchLanguages mFetchLanguages;
    private LoginUser mLoginUser;
    private RegisterUser mRegisterUser;
    private boolean mInitialized = false;
    private boolean mUserInitialized = false;
    private UsersDao mUsersDao;
    private Cursor mUserDetail;

    private TongueRepository(LanguagesDao languagesDao, UsersDao usersDao, FetchLanguages fetchLanguages,
                             RegisterUser registerUser, LoginUser loginUser, AppExecutors executors) {
        mUsersDao = usersDao;
        mLanguagesDao = languagesDao;
        mFetchLanguages = fetchLanguages;
        mRegisterUser = registerUser;
        mLoginUser = loginUser;
        mExecutors = executors;
        LiveData<Language[]> tongueLanguages = mFetchLanguages.getCurrentLanguages();
        //LiveData<[]> tongueUser = mLoginUser.getCurrentUser();

        /*Log.d(LOG_TAG, tongueUser.getEmail()+" current user ");
        if (tongueUser.getEmail() == null){
            Log.d(LOG_TAG, "current user is null ");
        }*/

        //insert user details to db
        //insertUser(tongueUser);

        /*
        * Why use observeForever()?

          observeForever() is very similar to observe, with one major difference, it is always considered
          active. Because of this, it does not take an object with a Lifecycle. Why are you using it here?
          TongueRepository is observing FetchLanguages; neither of these have an associated UI
          controller lifecycle, rather, they exist for the entire lifecycle of the app. Therefore,
          you can safely use observeForever().

        * */

        tongueLanguages.observeForever(newLanguagesFromNetwork -> mExecutors.diskIO().execute(() -> {
            // Insert our languages into Tongue database
            mLanguagesDao.insertLanguage(newLanguagesFromNetwork);

            Log.d(LOG_TAG, newLanguagesFromNetwork.length +" languages inserted");
        }));

    }

    public synchronized static TongueRepository getInstance(
            LanguagesDao languagesDao, UsersDao usersDao, FetchLanguages fetchLanguages,
            RegisterUser registerUser, LoginUser loginUser,
            AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new TongueRepository(languagesDao, usersDao, fetchLanguages,
                        registerUser, loginUser, executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    private synchronized void initializeData() {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) return;
        mInitialized = true;

        //  if (isFetchNeeded()) {
//}
        mExecutors.diskIO().execute(this::startFetchLanguageService);
    }

    private synchronized void initializeUser() {

        if (mUserInitialized) return;
        mUserInitialized = true;

        if (isLoginUser()){
            insertUser(mLoginUser.getCurrentUser());
        }

    }

    /**
     * Database related operations
     **/

    public LiveData<List<Language>> getAllLanguages(){
        initializeData();
        return mLanguagesDao.getAllLanguages();
    }

    public Cursor getUser(){
        initializeUser();
        //mUserDetail = mUsersDao.getUserDetails();
        mExecutors.diskIO().execute(() ->{
            mUserDetail = mUsersDao.getUserDetails();

            Log.d(LOG_TAG, "Getting user details from sqlite db");
        });

        return mUserDetail;
    }

    //a wrapper for the insert() method. Must be called on a non UI thread
    //or the app will crash
    public void insertUser (User user){
        //User tongueUser = mLoginUser.getCurrentUser();
        mExecutors.diskIO().execute(() ->{
            mUsersDao.insertUser(user);

            Log.d(LOG_TAG, user.getEmail()+" user inserted into db");
        });
    }

    //delete user details from db
    //required when user logs out of app
    public void deleteUser (){
        mUsersDao.deleteUser();
    }

    /**
     * Checks if there are enough days of future weather for the app to display all the needed data.
     *
     * @return Whether a fetch is needed
     */
    /*
    private boolean isFetchNeeded() {
        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();
        int count = mWeatherDao.countAllFutureWeather(today);
        return (count < WeatherNetworkDataSource.NUM_DAYS);
    }
    */

    private boolean isLoginUser() {
        User tongueUser = mLoginUser.getCurrentUser();

        return tongueUser.getEmail() != null;

    }

    /**
     * Network related operation
     */

    /*
     * call startFetchCategoryService() from FetchCategories which
     * creates and starts the IntentService.
     * */
    private void startFetchLanguageService() {
        mFetchLanguages.startFetchLanguageService();
    }

    //method to call service to login user
    public void loginTongueUser(String email, String password){
        mLoginUser.UserLogIn(email, password);
    }

    //method to register user in database
    //calls service
    public boolean registerTongueUser(String email, String password){
        return mRegisterUser.UserRegister(email, password);
    }

}
