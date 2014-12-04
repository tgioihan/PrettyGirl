package com.bestfunforever.app.prettygirl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bestfunforever.app.prettygirl.R;
import com.bestfunforever.app.prettygirl.model.Comment;
import com.facebook.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends BaseAdapter {

	private ArrayList<Comment> comments = new ArrayList<Comment>();
	private Context context;
	private OnClickListener likeCommentListenner;
	private OnClickListener unlikeCommentListenner;

	public CommentAdapter(Context context,
			OnClickListener likeCommentListenner,
			OnClickListener unlikeCommentListenner) {
		super();
		this.context = context;
		this.likeCommentListenner = likeCommentListenner;
		this.unlikeCommentListenner = unlikeCommentListenner;
	}

	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public Comment getItem(int arg0) {
		return comments.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.comment_item, arg2,false);
		}
		ProfilePictureView profilePictureView = (ProfilePictureView) convertView
				.findViewById(R.id.profileUser);
		TextView userName = (TextView) convertView
				.findViewById(R.id.txtUserName);
		TextView txtComment = (TextView) convertView
				.findViewById(R.id.txtComment);
		TextView txtTimestamp = (TextView) convertView
				.findViewById(R.id.txtTimestamp);
		TextView txtThich = (TextView) convertView.findViewById(R.id.txtThich);
		TextView txtBoThich = (TextView) convertView
				.findViewById(R.id.txtBoThich);
		TextView txtLikeCOmmentCount = (TextView) convertView
				.findViewById(R.id.txtLikeCOmmentCount);

		profilePictureView.setPresetSize(ProfilePictureView.SMALL);
		profilePictureView.setProfileId(comments.get(pos).getUser().getId());

		userName.setText(comments.get(pos).getUser().getName());
		txtComment.setText(comments.get(pos).getMessage());

		if (!comments.get(pos).isUser_likes()) {
			txtThich.setVisibility(View.VISIBLE);
			txtBoThich.setVisibility(View.GONE);
		} else {
			txtThich.setVisibility(View.GONE);
			txtBoThich.setVisibility(View.VISIBLE);
		}
		txtThich.setTag(pos);
		txtBoThich.setTag(pos);
		txtThich.setOnClickListener(likeCommentListenner);
		txtBoThich.setOnClickListener(unlikeCommentListenner);

		if (comments.get(pos).getLike_count() > 0) {
			txtLikeCOmmentCount.setVisibility(View.VISIBLE);
			txtLikeCOmmentCount.setText(comments.get(pos).getLike_count() + "");
		} else {
			txtLikeCOmmentCount.setVisibility(View.GONE);
		}
		txtLikeCOmmentCount.setTag(comments.get(pos).getLike_count());

		String timeStampt = convertTimeStamp(comments.get(pos)
				.getCreated_time());
		txtTimestamp.setText(timeStampt);
		convertView.setTag(pos);
		return convertView;
	}

	/**
	 * @param created_time : time created
	 * @return string
	 */
	private String convertTimeStamp(String created_time) {
		Integer[] integers = new Integer[] { 315360000, 31536000, 2628000,
				604800, 86400, 3600, 60, 1 };
		long datediff = new Date().getTime() / 1000
				- Long.parseLong(created_time);
		Log.e("", "datediff " + datediff);
		if (datediff > integers[0]) {
			return  datediff / integers[0] + " thap ky truoc";
		} else if (datediff > integers[1]) {
			return  datediff / integers[1] + " "
					+ context.getString(R.string.yearago);
		} else if (datediff > integers[2]) {
			return " " + datediff / integers[2] + " "
					+ context.getString(R.string.monthago);
		} else if (datediff > integers[3]) {
			return  datediff / integers[3] + " "
					+ context.getString(R.string.weekago);
		} else if (datediff > integers[4]) {
			return  datediff / integers[4] + " "
					+ context.getString(R.string.dayago);
		} else if (datediff > integers[5]) {
			return  datediff / integers[5] + " "
					+ context.getString(R.string.hourago);
		} else if (datediff > integers[6]) {
			return  datediff / integers[6] + " "
					+ context.getString(R.string.minuteago);
		} else if (datediff >= integers[7]) {
			return  " " + context.getString(R.string.now);
		}

		return null;
	}

	public void setData(ArrayList<Comment> list_comments) {
		comments.clear();
		comments.addAll(list_comments);
		notifyDataSetChanged();
	}

}
