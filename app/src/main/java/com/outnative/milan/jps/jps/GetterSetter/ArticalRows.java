package com.outnative.milan.jps.jps.GetterSetter;

/**
 * Created by milan on 1/9/2018.
 */
public class ArticalRows {
    public String date,title,views,likes,postId,type;

    public ArticalRows(String date, String title, String views, String likes, String postId,String type) {
        this.setDate(date);
        this.setTitle(title);
        this.setLikes(likes);
        this.setViews(views);
        this.setPostId(postId);
        this.setType(type);
    }

    public String getType() {
        return type;
    }

    public String getViews() {
        return views;
    }

    public String getTitle() {
        return title;
    }

    public String getPostId() {
        return postId;
    }

    public String getDate() {
        return date;
    }

    public String getLikes() {
        return likes;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
