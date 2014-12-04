package com.bestfunforever.app.prettygirl.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

public class Paging implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -40601467209104593L;

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	private String next;
	
	private String previous;

	public void rebbuildPagingNextCommentString() {
		String str = next;
		if (next != null) {
			Log.e("", "paging new " + str.substring(45, str.length()));
			String temp = str.substring(50, str.length());
			if (temp.startsWith("limit")) {
				String temp2 = str.substring(str.indexOf("limit"),
						str.indexOf("&acces"));
				String limitStr = "&"
						+ temp2.substring(temp2.indexOf("limit"),
								temp2.indexOf("&format"));
				String formatStr = temp2.substring(temp2.indexOf("format"),
						temp2.indexOf("&after"));
				String afterStr = temp2.substring(temp2.indexOf("&after"),
						temp2.length());
				Log.e("", "after "+ afterStr);
				afterStr = afterStr.replace("%3D", "=");
				String temp1 = str.substring(0, str.indexOf("limit"));
				String temp3 = str.substring(str.indexOf("&acces"),
						str.length());
				String rs = temp1 + formatStr + temp3 + limitStr + afterStr;
				this.next = rs.substring(43, str.length());
			}
			this.next = str.substring(43, str.length());
		}

	}
	
	public void rebbuildPagingPreviousCommentString() {
		String str = previous;
		if (previous != null) {
			Log.e("", "paging new " + str.substring(45, str.length()));
			String temp = str.substring(50, str.length());
			if (temp.startsWith("limit")) {
				String temp2 = str.substring(str.indexOf("limit"),
						str.indexOf("&acces"));
				String limitStr = "&"
						+ temp2.substring(temp2.indexOf("limit"),
								temp2.indexOf("&format"));
				String formatStr = temp2.substring(temp2.indexOf("format"),
						temp2.indexOf("&after"));
				String afterStr = temp2.substring(temp2.indexOf("&after"),
						temp2.length());
				String temp1 = str.substring(0, str.indexOf("limit"));
				String temp3 = str.substring(str.indexOf("&acces"),
						str.length());
				String rs = temp1 + formatStr + temp3 + limitStr + afterStr;
				this.previous = rs.substring(43, str.length());
			}
			this.previous = str.substring(43, str.length());
		}

	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}
}
