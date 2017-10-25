package com.example.model;

import com.example.model.exceptions.*;

import java.sql.Timestamp;


public final class Comment implements Comparable<Comment> {
    // ::::::::: main object characteristics :::::::::
    private long id = 0;
    private String content = null;
    private int likesCount = 0;
    private int dislikesCount = 0;
    private long postId = 0;
    private long userId = 0;
    private Timestamp datetime = null;
    // ::::::::: additional object characteristics :::::::::
    private static final int MAX_CONTENT_LENGTH = 500;
    private User sentBy = null;

    // ::::::::: constructor to be used when posting a new comment :::::::::
    public Comment(String content, long postId, long userId, Timestamp datetime, User sentBy) throws CommentException {
        this.setContent(content);
        this.setPostId(postId);
        this.setUserId(userId);
        this.setDatetime(datetime);
        this.setSentBy(sentBy);
    }

    // ::::::::: constructor to be used when loading an existing comment from db
    // :::::::::
    public Comment(long id, String content, int likesCount, int dislikesCount, long postId, long userId, Timestamp datetime,
                   User sentBy) throws CommentException {
        this(content, postId, userId, datetime, sentBy);
        this.setId(userId);
        this.setLikesCount(likesCount);
        this.setDislikesCount(dislikesCount);
    }

    // ::::::::: accessors :::::::::
    public long getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public int getLikesCount() {
        return this.likesCount;
    }

    public int getDislikesCount() {
        return this.dislikesCount;
    }

    public long getPostId() {
        return this.postId;
    }

    public long getUserId() {
        return this.userId;
    }

    public Timestamp getDatetime() {
        return this.datetime;
    }

    public User getSentBy() {
        return this.sentBy;
    }

    // ::::::::: mutators :::::::::
    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) throws CommentException {
        if (content != null) {
            if (content.length() <= MAX_CONTENT_LENGTH) {
                this.content = content;
            } else {
                throw new CommentException("Comment too long!");
            }
        } else {
            throw new CommentException("Invalid comment content!");
        }
    }

    public void setLikesCount(int likesCount) throws CommentException {
        if (likesCount >= 0) {
            this.likesCount = likesCount;
        } else {
            throw new CommentException("Invalid number of likes!");
        }
    }

    public void setDislikesCount(int dislikesCount) throws CommentException {
        if (dislikesCount >= 0) {
            this.dislikesCount = dislikesCount;
        } else {
            throw new CommentException("Invalid number of dislikes!");
        }
    }

    public void setPostId(long postId) throws CommentException {
        if (postId > 0) {
            this.postId = postId;
        } else {
            throw new CommentException("Invalid post id!");
        }
    }

    public void setUserId(long userId) throws CommentException {
        if (userId > 0) {
            this.userId = userId;
        } else {
            throw new CommentException("Invalid user id!");
        }
    }

    public void setDatetime(Timestamp datetime) throws CommentException {
        if (datetime != null) {
            this.datetime = datetime;
        } else {
            throw new CommentException("Invalid date/time!");
        }
    }

    public void setSentBy(User sentBy) throws CommentException {
        if (sentBy != null) {
            this.sentBy = sentBy;
        } else {
            throw new CommentException("Invalid sender!");
        }
    }

    public void incrementLikes() throws CommentException {
        this.setLikesCount(this.likesCount + 1);
    }

    public void incrementDislikes() throws CommentException {
        this.setDislikesCount(this.dislikesCount + 1);
    }

    @Override
    public int compareTo(Comment c) {
        return this.getDatetime().compareTo(c.getDatetime());
    }

}