package com.outnative.milan.jps.jps.GetterSetter;

/**
 * Created by milan on 1/7/2018.
 */

public class PoemRows {
    public String title,date,body,views,like,postId,username;

    public PoemRows(String title, String date, String body, String views, String like, String postId, String username) {
        this.setBody(body);
        this.setDate(date);
        this.setLike(like);
        this.setPostId(postId);
        this.setTitle(title);
        this.setUsername(username);
        this.setViews(views);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }

    public String getLike() {
        return like;
    }

    public String getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public String getViews() {
        return views;
    }
}
