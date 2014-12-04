package com.bestfunforever.app.prettygirl.util;


import com.bestfunforever.app.prettygirl.model.Photo;

import java.util.Comparator;

public class TimePhotoComparator implements Comparator<Photo> {

	@Override
	public int compare(Photo lhs, Photo rhs) {
		int name1 = lhs.getCreated();
		int name2 = rhs.getCreated();
		if (name1 > name2)
			return -1;
		else if (name1 < name2)
			return 1;
		else
			return 0;
	}
}
