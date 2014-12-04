package com.bestfunforever.app.prettygirl.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6493521809185989788L;
	private LikeInfo likeInfo;
	private ArrayList<User> user_liked;
	private String name;
	private User user_post;
	private String source;
	private CommentInfo commentInfo;
	private Paging commentPaging;
	private String id;
	private String link;
	private int like_count;
	private int comment_count;
	private boolean is_user_like = false;
	private int created;
	private String album_object_id;

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public boolean isFavorite = false;

	public String getSource() {
		return source;
	}

	public void setSource(String image) {
		this.source = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CommentInfo getCommentInfo() {
		return commentInfo;
	}

	public void setCommentInfo(CommentInfo commentInfo) {
		this.commentInfo = commentInfo;
	}

	public LikeInfo getLikeInfo() {
		return likeInfo;
	}

	public void setLikeInfo(LikeInfo likeInfo) {
		this.likeInfo = likeInfo;
	}

	public User getUser_post() {
		return user_post;
	}

	public void setUser_post(User user_post) {
		this.user_post = user_post;
	}

	public ArrayList<User> getUser_liked() {
		return user_liked;
	}

	public void setUser_liked(ArrayList<User> user_liked) {
		this.user_liked = user_liked;
	}

	public ArrayList<Comment> getList_comments() {
		return list_comments;
	}

	public void setList_comments(ArrayList<Comment> list_comments) {
		this.list_comments = list_comments;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Paging getCommentPaging() {
		return commentPaging;
	}

	public void setCommentPaging(Paging commentPaging) {
		this.commentPaging = commentPaging;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public int getLike_count() {
		return like_count;
	}

	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}

	public boolean isIs_user_like() {
		return is_user_like;
	}

	public void setIs_user_like(boolean is_user_like) {
		this.is_user_like = is_user_like;
	}

	public String getAlbum_object_id() {
		return album_object_id;
	}

	public void setAlbum_object_id(String album_object_id) {
		this.album_object_id = album_object_id;
	}

	private ArrayList<Comment> list_comments;

}
