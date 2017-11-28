package com.krrish.mapprr_assignment.exploregit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContributorDetailsActivity extends AppCompatActivity {
    private LinearLayout contributionDetailsList;
    private ImageView contributionDetailsImage;
    private String contributorImageUrl, contributorReposUrl;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributor_details);
        initViews();
    }

    void initViews(){
        Bundle bundle = getIntent().getExtras();
        contributorImageUrl = bundle.getString("contributor_image");
        contributorReposUrl = bundle.getString("repo_url");
        contributionDetailsList = (LinearLayout) findViewById(R.id.contributionDetailsList);
        contributionDetailsImage = (ImageView) findViewById(R.id.contributorDetailsImage);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching contributions...");
        Picasso.with(ContributorDetailsActivity.this)
                .load(contributorImageUrl)
                .into(contributionDetailsImage);
        new RepoListBackground(ContributorDetailsActivity.this, contributorReposUrl).execute();
    }

    class RepoListBackground extends AsyncTask<String, Void, JSONArray>{
        Context context;
        String url;

        RepoListBackground(Context context, String url){
            this.context = context;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }
        @Override
        protected JSONArray doInBackground(String... strings) {
            String serviceUrl = this.url.toString();

            HttpURLConnection c = null;
            try {
                URL u = new URL(serviceUrl);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.setConnectTimeout(500000);
                c.setReadTimeout(500000);
                c.connect();
                int status = c.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        //return sb.toString();
                        try {
                            JSONArray jsonArray = new JSONArray(sb.toString());
                            return jsonArray;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                }

            } catch (MalformedURLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray){
            super.onPostExecute(jsonArray);
            progressDialog.dismiss();
            loadRepositories(jsonArray);
        }
    }

    void loadRepositories(JSONArray jsonArray){
        try{
            for (int i=0; i<jsonArray.length(); i++){
                final JSONObject itemObject = jsonArray.getJSONObject(i);
                final JSONObject ownerObject = itemObject.getJSONObject("owner");
                LinearLayout linearLayout = new LinearLayout(this);
                TextView textView1 = new TextView(this);
                TextView textView2 = new TextView(this);
                textView1.setText("** Repo " + i);
                textView2.setText(" ( " + itemObject.getString("name") + " )");
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                textView1.setTextColor(Color.parseColor("#000000"));
                textView2.setTextColor(Color.parseColor("#FF3046D9"));
                linearLayout.addView(textView1);
                linearLayout.addView(textView2);
                contributionDetailsList.addView(linearLayout);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ContributorDetailsActivity.this, RepoDetailsActivity.class);
                        try {
                            intent.putExtra("avatar_url", ownerObject.getString("avatar_url"));
                            intent.putExtra("repo_name", itemObject.getString("name"));
                            intent.putExtra("project_link", itemObject.getString("html_url"));
                            intent.putExtra("description", itemObject.getString("description"));
                            intent.putExtra("contributors_url", itemObject.getString("contributors_url"));
                            startActivity(intent);
                        }catch (JSONException je){
                            je.printStackTrace();
                        }
                    }
                });
            }
        }catch (JSONException je){
            je.printStackTrace();
        }
    }
}
