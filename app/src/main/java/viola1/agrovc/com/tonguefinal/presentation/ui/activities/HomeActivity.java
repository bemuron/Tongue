package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

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
import viola1.agrovc.com.tonguefinal.view.Adapter;
import viola1.agrovc.com.tonguefinal.view.LanguageSearch;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Adapter.LanguageSearchListListener {
    private static final String TAG = HomeActivity.class.getSimpleName();

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRV;
    private Adapter mAdapter;
    private Context context;
    SearchView searchView = null;
    java.util.List<String> IdList = new ArrayList<>();
    List<LanguageSearch> languageSearchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup and Handover data to recyclerview
        mRV = findViewById(R.id.language_list);
        mAdapter = new Adapter(HomeActivity.this, languageSearchList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRV.setLayoutManager(mLayoutManager);
        mRV.setItemAnimator(new DefaultItemAnimator());
        mRV.addItemDecoration(new DividerItemDecoration(HomeActivity.this, LinearLayoutManager.VERTICAL));
        mRV.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) HomeActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(HomeActivity.this.getComponentName()));
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
        ProgressDialog pdLoading = new ProgressDialog(HomeActivity.this);

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

                        Toast.makeText(HomeActivity.this, "No Results found for entered query", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(HomeActivity.this, "Ooops something went wrong. Please" +
                        "try again", Toast.LENGTH_SHORT).show();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
            }
        });
    }

    // Create class AsyncFetch
   /* private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(HomeActivity.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;

        public AsyncFetch(String searchQuery){
            this.searchQuery=searchQuery;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(AppProperties.TONGUE_LANGUAGE_SEARCH);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", searchQuery);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                Log.e(TAG, e1.getMessage());
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            pdLoading.dismiss();

            pdLoading.dismiss();
            if(result.equals("no rows")) {
                MDToast mdToast = MDToast.makeText(HomeActivity.this, "No Results found for entered query", Toast.LENGTH_LONG,MDToast.TYPE_INFO);

                mdToast.show();

            }else{

                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);

                        //clear the previous search list if it has content
                        if (languageSearchList != null){
                            languageSearchList.clear();
                        }
                        LanguageSearch languageSearch = new LanguageSearch();
                        languageSearch.setLanguage_id(json_data.getInt("language_id"));
                        languageSearch.setName(json_data.getString("name"));
                        languageSearch.setLanguage_code(json_data.getString("language_code"));

                        languageSearchList.add(languageSearch);
                        mAdapter.notifyDataSetChanged();
                        Log.d(TAG, "language list size = " +languageSearchList.size());
                    }

                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    MDToast mdToast = MDToast.makeText(HomeActivity.this,  e.toString(), Toast.LENGTH_LONG, MDToast.TYPE_ERROR);

                    mdToast.show();
                    Log.e(TAG, e.getMessage());

                }

            }

        }

    }*/

    //handle clicks on the search results items
    @Override
    public void onResultRowClicked(int position){
        //get the language clicked
        LanguageSearch languageSearch = languageSearchList.get(position);

        //start Tutor Activity passing in the id of the language clicked
        Intent intent = new Intent(HomeActivity.this, TutorActivity.class);
        intent.putExtra("language_id", languageSearch.getLanguage_id());
        intent.putExtra("language_name", languageSearch.getName());
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
