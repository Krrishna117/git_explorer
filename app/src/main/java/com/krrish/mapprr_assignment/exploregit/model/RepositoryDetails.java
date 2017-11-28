package com.krrish.mapprr_assignment.exploregit.model;

/**
 * Created by Krrish on 24-11-2017.
 */

public class RepositoryDetails {
    private String avatarUrl, name, fullName, watchersCount, commitsCount, projectLink, description, contributorsUrl;

    public RepositoryDetails(String avatarUrl, String name, String fullName, String watchersCount, String commitsCount, String projectLink, String description, String contributorsUrl) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.fullName = fullName;
        this.watchersCount = watchersCount;
        this.commitsCount = commitsCount;
        this.projectLink = projectLink;
        this.description = description;
        this.contributorsUrl = contributorsUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(String watchersCount) {
        this.watchersCount = watchersCount;
    }

    public String getCommitsCount() {
        return commitsCount;
    }

    public void setCommitsCount(String commitsCount) {
        this.commitsCount = commitsCount;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContributorsUrl() {
        return contributorsUrl;
    }

    public void setContributorsUrl(String contributorsUrl) {
        this.contributorsUrl = contributorsUrl;
    }
}
