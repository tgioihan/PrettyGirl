package com.bestfunforever.app.prettygirl.model;

import java.io.Serializable;

public class LikeInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8105057624314890350L;
	private int like_count;

	public int getLike_count() {
		return like_count;
	}

	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}
}
