package com.krrish.mapprr_assignment.exploregit;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Krrish on 23-11-2017.
 */

public class RepositoriesResponse {
    @SerializedName("total_count")
    private int totalCount;
    /*@SerializedName("name")
    private String name;
    @SerializedName("full_name")
    private String fullName;*/
    @SerializedName("items")
    private JsonArray repoResults;

    public int getTotalCount(){
        return this.totalCount;
    }

    public void setTotalCount(int totalCount){
        this.totalCount = totalCount;
    }

    public JsonArray getRepoResults(){
        return repoResults;
    }

    public void setRepoResults(JsonArray repoResults){
        this.repoResults = repoResults;
    }
}
