package com.krrish.mapprr_assignment.exploregit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.krrish.mapprr_assignment.exploregit.R;
import com.krrish.mapprr_assignment.exploregit.RepoDetailsActivity;
import com.krrish.mapprr_assignment.exploregit.model.RepositoryDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Krrish on 24-11-2017.
 */

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.MyViewHolder> {
    private List<RepositoryDetails> repositories;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView avatar;
        public TextView repoName, repoFullName, repoWatchersCount, repoCommitsCount;
        public MyViewHolder(View view){
            super(view);
            avatar = (ImageView) view.findViewById(R.id.avatarImage);
            repoName = (TextView) view.findViewById(R.id.repoName);
            repoFullName = (TextView) view.findViewById(R.id.repoFullName);
            repoWatchersCount = (TextView) view.findViewById(R.id.watchersCount);
            repoCommitsCount = (TextView) view.findViewById(R.id.commitsCount);
        }
    }
    public RepositoriesAdapter(List<RepositoryDetails> repositories){
        this.repositories = repositories;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        context = parent.getContext();
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_result_view, parent, false);
        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "HERE I AM", Toast.LENGTH_SHORT).show();
            }
        });*/
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        final RepositoryDetails repositoryDetails = repositories.get(position);
        Picasso.with(this.context)
                .load(repositoryDetails.getAvatarUrl())
//                .resize(30,30)
//                .centerCrop()
                .into(holder.avatar);
//        holder.avatar.setImageURI(repositoryDetails.getAvatarUrl());
        holder.repoName.setText(repositoryDetails.getName());
        holder.repoFullName.setText(repositoryDetails.getFullName());
        holder.repoWatchersCount.setText(repositoryDetails.getWatchersCount());
        holder.repoCommitsCount.setText(repositoryDetails.getCommitsCount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RepoDetailsActivity.class);
                intent.putExtra("avatar_url", repositoryDetails.getAvatarUrl());
                intent.putExtra("repo_name", repositoryDetails.getName());
                intent.putExtra("project_link", repositoryDetails.getProjectLink());
                intent.putExtra("description", repositoryDetails.getDescription());
                intent.putExtra("contributors_url", repositoryDetails.getContributorsUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return repositories.size();
    }
}
