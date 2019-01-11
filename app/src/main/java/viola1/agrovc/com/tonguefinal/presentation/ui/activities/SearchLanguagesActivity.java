package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.constants.EnumAppMessages;
import viola1.agrovc.com.tonguefinal.data.network.SearchResult;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.presentation.adapters.Adapter;
import viola1.agrovc.com.tonguefinal.view.LanguageSearch;

public class SearchLanguagesActivity extends AppCompatActivity
        implements Adapter.LanguageSearchListListener {

    private static final String TAG = SearchLanguagesActivity.class.getSimpleName();
    private RecyclerView mRV;
    private Adapter mAdapter;
    SearchView searchView = null;
    private String becomeTutor;
    List<LanguageSearch> languageSearchList = new ArrayList<>();
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_search_languages);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        //get intent from which this activity is called
        becomeTutor = getIntent().getStringExtra("becomeTutor");

        mRV = findViewById(R.id.language_list);
        mRV.setLayoutManager(new LinearLayoutManager(this));
        mRV.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new Adapter(SearchLanguagesActivity.this, languageSearchList, this);
        mRV.setAdapter(mAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//close onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_main, menu);

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) SearchLanguagesActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchItem.expandActionView();
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchLanguagesActivity.this.getComponentName()));
            searchView.setIconified(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        if (id == R.id.action_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
        // Get search query and create object of class AsyncFetch
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchLanguage(query);
            //new HomeActivity.AsyncFetch(query).execute();
            if (searchView != null) {
                searchView.clearFocus();
            }

        }
    }

    private void searchLanguage(String searchQuery){
        ProgressDialog pdLoading = new ProgressDialog(SearchLanguagesActivity.this);

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<SearchResult> call = service.searchForLanguage(searchQuery);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {

                if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    pdLoading.dismiss();

                    if (response.body().getLanguage_search().size() > 0){

                        //clear the previous search list if it has content
                        if (languageSearchList != null) {
                            languageSearchList.clear();
                        }

                        for (int i = 0; i < response.body().getLanguage_search().size(); i++) {

                            LanguageSearch searchResult = new LanguageSearch();
                            searchResult.setLanguage_id(response.body().getLanguage_search().get(i).getLanguage_id());
                            searchResult.setName(response.body().getLanguage_search().get(i).getName());
                            searchResult.setLanguage_code(response.body().getLanguage_search().get(i).getLanguage_code());

                            languageSearchList.add(searchResult);
                        }

                        mAdapter.notifyDataSetChanged();
                        //Log.d(TAG, "language list size = " +languageSearchList.size());

                        //Log.d(TAG, "list size from network "+response.body().getLanguage_search().size());
                        //Log.d(TAG, "lang name from network "+response.body().getLanguage_search().get(0).getName());

                    }else if (response.body().getLanguage_search().size() == 0){

                        Toast.makeText(SearchLanguagesActivity.this, "No Results found for entered query", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(response.code() == AppNums.STATUS_COD_FILE_NOT_FOUND){
                    pdLoading.dismiss();

                    Log.e(TAG, EnumAppMessages.ERROR_RESOURCE_NOT_FOUND.getValue());


                }else{
                    pdLoading.dismiss();


                    //generalMethods.showLocationDialog(Login.this,EnumAppMessages.LOGIN_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());
                    Log.e(TAG, EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());

                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                pdLoading.dismiss();

                Toast.makeText(SearchLanguagesActivity.this, "Ooops something went wrong. Please" +
                        " try again", Toast.LENGTH_SHORT).show();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
            }
        });
    }

    //handle clicks on the search results items
    @Override
    public void onResultRowClicked(int position){
        //get the language clicked
        LanguageSearch languageSearch = languageSearchList.get(position);

        if (becomeTutor != null && becomeTutor.equals("becomeTutor")){
            Intent intent = new Intent();
            intent.putExtra("language_id", languageSearch.getLanguage_id());
            intent.putExtra("language_name", languageSearch.getName());
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else {
            //start Tutor Activity passing in the id of the language clicked
            Intent intent = new Intent(SearchLanguagesActivity.this, TutorActivity.class);
            intent.putExtra("language_id", languageSearch.getLanguage_id());
            intent.putExtra("language_name", languageSearch.getName());
            startActivity(intent);
            finish();
        }
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
        session.logoutUser();
        //mViewModel.delete();
    }
}
