package com.example.model;

import com.example.model.exceptions.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Comment implements Comparable<Comment> {
	// ::::::::: main object characteristics :::::::::
	private long id = 0;
	private String content = null;
	private int likesCount = 0;
	private int dislikesCount = 0;
	private long postId = 0;
	private long userId = 0;
	private Timestamp datetime = null;
	private String datetimeString = null; // trust me, i really need it
	// ::::::::: additional object characteristics :::::::::
	private static final int MAX_CONTENT_LENGTH = 500;
	private User sentBy = null;
    private HashSet<Long> peopleLiked = new HashSet<Long>();
    private HashSet<Long> peopleDisliked = new HashSet<Long>();


	// ::::::::: constructor to be used when posting a new comment :::::::::
	public Comment(String content, long postId, long userId, User sentBy) throws CommentException {
		this.setContent(content);
		this.setPostId(postId);
		this.setUserId(userId);
		this.setSentBy(sentBy);
	}

	// TO BE PARTICULARLY USED WHEN POSTING A NEW COMMENT 
	public Comment(long id, String content, int likesCount, int dislikesCount, long postId, long userId,
			Timestamp datetime) throws CommentException {
		this.setContent(content);
		this.setPostId(postId);
		this.setUserId(userId);
		this.setId(id);
		this.setLikesCount(likesCount);
		this.setDislikesCount(dislikesCount);
		this.setDatetime(datetime);
		this.datetimeString = this.datetime.toString();
	}

	public String getDatetimeString() {
		return this.datetimeString;
	}

	public void setDatetimeString(String datetimeString) {
		this.datetimeString = datetimeString;
	}

	// ::::::::: constructor to be used when loading an existing comment from db
	// :::::::::
	public Comment(long id, String content, int likesCount, int dislikesCount, long postId, long userId,
			Timestamp datetime, User sentBy) throws CommentException {
		this(content, postId, userId, sentBy);
		this.setId(id);
		this.setUserId(userId);
		this.setLikesCount(likesCount);
		this.setDislikesCount(dislikesCount);
		this.setDatetime(datetime);
		this.datetimeString = this.datetime.toString();
		this.setSentBy(sentBy);
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
		return c.getDatetime().compareTo(this.getDatetime());
	}

	//LIKE/DISLIKE FUNCTIONALITY
	public void addPersonLiked(long userId) {
        if(this.peopleLiked==null){
            this.peopleLiked=new HashSet<>();
        }
       this.peopleLiked.add(userId);
    }

    public void removePersonLiked(long userId) {
        if(this.peopleLiked==null){
            this.peopleLiked=new HashSet<>();
        }
        this.peopleLiked.remove(userId);
    }

    public void removePersonDisliked(long userId) {
        if(this.peopleDisliked==null){
            this.peopleDisliked=new HashSet<>();
        }
        this.peopleDisliked.remove(userId);
    }

    public void addPersonDisliked(long userId) {
        if(this.peopleDisliked==null){
            this.peopleDisliked=new HashSet<>();
        }
        this.peopleDisliked.add(userId);
    }
	
    public Set<Long> getPeopleLiked() {
        return Collections.unmodifiableSet(this.peopleLiked);
    }

    public void setPeopleLiked(HashSet<Long> peopleLiked) {
        this.peopleLiked = peopleLiked;
    }

    public Set<Long> getPeopleDisliked() {
        return Collections.unmodifiableSet(this.peopleDisliked);
    }

    public void setPeopleDisliked(HashSet<Long> peopleDisliked) {
        this.peopleDisliked = peopleDisliked;
    }
}