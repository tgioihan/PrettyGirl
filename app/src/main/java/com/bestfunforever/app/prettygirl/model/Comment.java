package com.bestfunforever.app.prettygirl.model;

import java.io.Serializable;

public class Comment implements Serializable {

	private String id;
	private String message;
	private boolean can_remove;

	private User user;
	private String created_time;
	private int like_count;
	private boolean user_likes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isCan_remove() {
		return can_remove;
	}

	public void setCan_remove(boolean can_remove) {
		this.can_remove = can_remove;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public int getLike_count() {
		return like_count;
	}

	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}

	public boolean isUser_likes() {
		return user_likes;
	}

	public void setUser_likes(boolean user_likes) {
		this.user_likes = user_likes;
	}

}
