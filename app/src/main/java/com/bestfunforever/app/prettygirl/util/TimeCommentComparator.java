package com.bestfunforever.app.prettygirl.util;


import com.bestfunforever.app.prettygirl.model.Comment;

import java.util.Comparator;

public class TimeCommentComparator implements Comparator<Comment> {

	@Override
	public int compare(Comment lhs, Comment rhs) {
		int name1 = Integer.valueOf((lhs).getCreated_time());
		int name2 = Integer.valueOf((rhs).getCreated_time());
		if (name1 > name2)
			return 1;
		else if (name1 < name2)
			return -1;
		else
			return 0;
	}
}
