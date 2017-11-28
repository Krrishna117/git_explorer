package com.krrish.mapprr_assignment.exploregit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.krrish.mapprr_assignment.exploregit.HomeActivity.gitHubHeaderUrl;

public class RepoDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private String avatarUrl, repoName, strProjectLink, description, contributors;
    private ImageView repoImage;
    private TextView name, projectLink, repoDescription, repoContributors;
    private LinearLayout contributorsLayout;
    private JSONArray contributorsArray;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);
        initViews();
    }

    void initViews() {
        Bundle bundle = getIntent().getExtras();
        avatarUrl = bundle.getString("avatar_url");
        repoName = bundle.getString("repo_name");
        strProjectLink = bundle.getString("project_link");
        description = bundle.getString("description");
        contributors = bundle.getString("contributors_url");
        repoImage = (ImageView) findViewById(R.id.repoDetailsImage);
        name = (TextView) findViewById(R.id.repoDetailsName);
        projectLink = (TextView) findViewById(R.id.repoDetailsLink);
        repoDescription = (TextView) findViewById(R.id.repoDetailsDescription);
        repoContributors = (TextView) findViewById(R.id.contributorName);
        contributorsLayout = (LinearLayout) findViewById(R.id.contributorsView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Repository...");
        new ContributorsBackGround(RepoDetailsActivity.this, contributors).execute();

        Picasso.with(RepoDetailsActivity.this)
                .load(avatarUrl)
                .into(repoImage);
        name.setText(repoName);
        repoDescription.setText(description);
        projectLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repoDetailsLink:
                Intent intent = new Intent(RepoDetailsActivity.this, RepoWebPage.class);
                intent.putExtra("page_url", strProjectLink);
                startActivity(intent);
                break;
        }
    }

    class ContributorsBackGround extends AsyncTask<String, Void, JSONArray> {
        Context context;
        String url;

        ContributorsBackGround(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... voids) {
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
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            progressDialog.dismiss();
            loadContributors(jsonArray);
            /*for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    CircleImageView circleImageView = new CircleImageView(this.context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
                    circleImageView.setLayoutParams(layoutParams);
                    Picasso.with(this.context)
                            .load(jsonArray.getJSONObject(i).getString("avatar_url"))
                            .into(circleImageView);
                    TextView textView = new TextView(this.context);
                    textView.setText(jsonArray.getJSONObject(i).getString("login"));
                    textView.setGravity(Gravity.CENTER);
                    LinearLayout linearLayout = new LinearLayout(this.context);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.addView(circleImageView);
                    linearLayout.addView(textView);
                    linearLayout.setPadding(10 , 10, 10,10);
                    contributorsLayout.addView(linearLayout);
                    contributorsLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(RepoDetailsActivity.this, ContributorDetailsActivity.class);
                            intent.putExtra("contributor_image", jsonArray.getJSONObject(i).getString("avatar_url"));
                            intent.putExtra("repo_url",)
                        }
                    });
                    repoContributors.setText(jsonArray.getJSONObject(i).getString("login"));
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }*/
        }
    }

    void loadContributors(JSONArray jsonArray){
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                CircleImageView circleImageView = new CircleImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
                circleImageView.setLayoutParams(layoutParams);
                Picasso.with(this)
                        .load(jsonArray.getJSONObject(i).getString("avatar_url"))
                        .into(circleImageView);
                TextView textView = new TextView(this);
                textView.setText(jsonArray.getJSONObject(i).getString("login"));
                textView.setGravity(Gravity.CENTER);
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(circleImageView);
                linearLayout.addView(textView);
                linearLayout.setPadding(10 , 10, 10,10);
                contributorsLayout.addView(linearLayout);
                final String contributorImageUrl = jsonArray.getJSONObject(i).getString("avatar_url");
                final String reposListArray = jsonArray.getJSONObject(i).getString("repos_url");
                contributorsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RepoDetailsActivity.this, ContributorDetailsActivity.class);
                        intent.putExtra("contributor_image", contributorImageUrl);
                        intent.putExtra("repo_url",reposListArray);
                        startActivity(intent);
                    }
                });
                repoContributors.setText(jsonArray.getJSONObject(i).getString("login"));
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
    }
}
