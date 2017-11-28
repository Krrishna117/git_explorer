package com.krrish.mapprr_assignment.exploregit;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Krrish on 23-11-2017.
 */

public interface RetrofitInterface {
    @GET("repositories")
    Call<RepositoriesResponse> getRepositories(@Header("User-Agent") String gitHubHeaderUrl, @Query("q") String queryRepoName, @Query("per_page") int resultsCount);

    @GET("contributors")
    Call<JSONArray> getContributors();
}
