package com.krrish.mapprr_assignment.exploregit;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;
import com.krrish.mapprr_assignment.exploregit.adapter.RepositoriesAdapter;
import com.krrish.mapprr_assignment.exploregit.model.RepositoryDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    EditText enterRepoName;
    Button getRepoResults;
    public static final String gitHubHeaderUrl = "https://api.github.com/meta";
    public static final String BASE_URL = "https://api.github.com/search/";
    public JSONArray searchResult;
    private List<RepositoryDetails> repositoryDetails = new ArrayList<>();
    private RecyclerView recyclerView;
    private RepositoriesAdapter repositoriesAdapter;
    Response<RepositoriesResponse> responseNew;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
    }

    void initViews(){
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Fetching Results...");
        enterRepoName = (EditText) findViewById(R.id.enterRepoName);
        getRepoResults = (Button) findViewById(R.id.getRepoResults);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        repositoriesAdapter = new RepositoriesAdapter(repositoryDetails);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(repositoriesAdapter);
        getRepoResults.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.getRepoResults:
                String enteredRepoName = enterRepoName.getText().toString().trim();
                if(enteredRepoName.equals("")){
                    Snackbar snackbar = Snackbar.make(view, "Please enter your KEYWORD...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    snackbar.show();
                }
                else
                    getSearchResults(enteredRepoName);
                break;
        }
    }

    void getSearchResults(String searchKeyword){

        RetrofitInterface apiService = RetrofitInstance.getClient(BASE_URL).create(RetrofitInterface.class);
        Call<RepositoriesResponse> call = apiService.getRepositories(gitHubHeaderUrl, searchKeyword, 10);
        progressDialog.show();
        call.enqueue(new Callback<RepositoriesResponse>() {
            @Override
            public void onResponse(Call<RepositoriesResponse> call, Response<RepositoriesResponse> response) {
                progressDialog.dismiss();
                responseNew = response;
//                searchResult = response.body().getRepoResults();
                try{
                    searchResult = new JSONArray(response.body().getRepoResults().toString());
                    JSONArray sortedJSONArray = new JSONArray();
                    List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
                    for(int i=0; i<searchResult.length(); i++){
                        jsonObjects.add(searchResult.getJSONObject(i));
                    }
                    Collections.sort(jsonObjects, new Comparator<JSONObject>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public int compare(JSONObject a, JSONObject b) {
                            int compare = 0;
                            try{
                               int valA = a.getInt("watchers");
                               int valB = b.getInt("watchers");
                               compare = Integer.compare(valB, valA);
                            }catch (JSONException je){
                                je.printStackTrace();
                            }
                            return compare;
                        }
                    });
                    for(int i=0; i<searchResult.length(); i++){
                        sortedJSONArray.put(jsonObjects.get(i));
                    }
                    prepareRepositoryData(sortedJSONArray);
//                    prepareRepositoryData(searchResult);
                }catch (JSONException je){
                    je.printStackTrace();
                }
//                Log.e("Total Count", String.valueOf(response.body().getTotalCount()));
            }

            @Override
            public void onFailure(Call<RepositoriesResponse> call, Throwable t) {
                progressDialog.dismiss();
//                Log.e("Failure Case", t.toString());
            }
        });
    }

    void prepareRepositoryData(JSONArray searchResult){
        for (int i=0; i<searchResult.length(); i++){
            try {
                JSONObject itemObject = searchResult.getJSONObject(i);
                JSONObject ownerObject = itemObject.getJSONObject("owner");
                repositoryDetails.add(new RepositoryDetails(ownerObject.getString("avatar_url"), itemObject.getString("name"),
                        itemObject.getString("full_name"), String.valueOf(itemObject.getInt("watchers")), itemObject.getString("forks_count"),
                        itemObject.getString("html_url"), itemObject.getString("description"), itemObject.getString("contributors_url")));
                repositoriesAdapter.notifyDataSetChanged();
//                Log.v("Watcher Count", String.valueOf(itemObject.getInt("watchers")));
            }catch (JSONException je){
                je.printStackTrace();
            }
        }
    }
}
