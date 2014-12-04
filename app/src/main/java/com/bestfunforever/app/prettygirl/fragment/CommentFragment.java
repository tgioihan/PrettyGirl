package com.bestfunforever.app.prettygirl.fragment;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lib.core.activity.facebook.listener.IActionLikeFacebook;
import com.android.lib.core.activity.facebook.listener.IPostCommentFacebook;
import com.android.lib.core.activity.facebook.listener.IUserFaceBookListenner;
import com.android.lib.core.fragment.BaseFragment;
import com.android.lib.core.util.DebugLog;
import com.bestfunforever.app.prettygirl.MainActivity;
import com.bestfunforever.app.prettygirl.R;
import com.bestfunforever.app.prettygirl.adapter.CommentAdapter;
import com.bestfunforever.app.prettygirl.model.Comment;
import com.bestfunforever.app.prettygirl.model.Photo;
import com.bestfunforever.app.prettygirl.model.User;
import com.bestfunforever.app.prettygirl.util.CommonUtils;
import com.bestfunforever.app.prettygirl.util.TimeCommentComparator;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CommentFragment extends BaseFragment {
	private EditText edittextComment;
    private TextView likeFbTextView;
	private TextView commentFbTextView;
	private TextView loadingText;
	private ListView mListView;
	private LinearLayout loadCommentLL;
	private LinearLayout loadmorecommentll;
	private ProgressBar loadmorecommentprogress;
	private TextView loadingmorecommenttxt;
	private CommentAdapter mAdapter;
	private String photoId;
	private boolean loadComment = false;
	private int commentCount;
	
	private int commentOffset;

	private String source;

	private ArrayList<Comment> comments = new ArrayList<Comment>();
	protected String linkPhoto;
	private int postionOfPhoto;

	/**
	 * like a comment listenner
	 */
	private OnClickListener likeCommentListenner = new OnClickListener() {

		@Override
		public void onClick(View view) {
			final int pos = (Integer) view.getTag();
			if (comments != null && comments.size() > pos) {
				final String commentId = comments.get(pos).getId();
                likeComment(commentId, pos);
			}

		}
	};

	/**
	 * unlike a comment listenner
	 */
	private OnClickListener unlikeCommentListenner = new OnClickListener() {

		@Override
		public void onClick(View view) {
			final int pos = (Integer) view.getTag();
			if (comments != null && comments.size() > pos) {
				final String commentId = comments.get(pos).getId();
                unLikeComment(commentId, pos);
			}
		}
	};


    @Override
    protected void preOncreateView() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.commentfragment;
    }

    @Override
    protected void initView(View view) {
        edittextComment = (EditText) view.findViewById(R.id.edittextComment);
        Button btnComment = (Button) view.findViewById(R.id.btnComment);
        LinearLayout likeFb = (LinearLayout) view.findViewById(R.id.imageLike);
        loadCommentLL = (LinearLayout) view.findViewById(R.id.loadcommentll);
        loadmorecommentll = (LinearLayout) view
                .findViewById(R.id.loadmorecommentll);
        LinearLayout commentFb = (LinearLayout) view.findViewById(R.id.imageComment);
        ImageView wallpapperFb = (ImageView) view.findViewById(R.id.wallpaper);
        ImageView shareFb = (ImageView) view.findViewById(R.id.shareFB);
        likeFbTextView = (TextView) view.findViewById(R.id.txtLike);
        loadingmorecommenttxt = (TextView) view
                .findViewById(R.id.loadingmorecommenttxt);
        commentFbTextView = (TextView) view.findViewById(R.id.txtComment);
        loadingText = (TextView) view.findViewById(R.id.loadingtxt);
        mListView = (ListView) view.findViewById(R.id.list);
        loadmorecommentprogress = (ProgressBar) view
                .findViewById(R.id.loadmorecommentprogress);

        btnComment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String msg = edittextComment.getText().toString();
                if (msg != null && !msg.equals("")) {
                    if(((MainActivity) getActivity()).getFacebookHelper().getActiveUserFacebook()==null){
                        ((MainActivity) getActivity()).getFacebookHelper().getUserInfo(new IUserFaceBookListenner() {
                            @Override
                            public void onGetUserInfoSuccess(GraphUser graphUser) {

                            }
                        });
                    }else{
                        postComment(msg);
                    }

                }
            }
        });

        loadingmorecommenttxt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (comments.size() < commentCount) {
                    loadmorecommentll.setVisibility(View.VISIBLE);
                    loadingmorecommenttxt.setText(getString(R.string.loading));
                    loadmorecommentprogress.setVisibility(View.GONE);
                    loadCommen();
                } else {
                    loadmorecommentll.setVisibility(View.GONE);
                }
            }
        });

        likeFb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DebugLog.d( "click like fb ");
                likePhoto();
            }
        });

        commentFb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DebugLog.d(  "click comment fb ");
                ((MainActivity) getActivity()).setPage(1);
            }
        });

        wallpapperFb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DebugLog.d(  "click wallpp fb ");
                showMessageConfirmSetWallPapper();
            }
        });

        shareFb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DebugLog.d(  "click share fb ");
                ((MainActivity) getActivity()).getFacebookHelper().shareFacebook(getString(R.string.app_name), null, getString(R.string.app_name), null, linkPhoto, source, null, null);
            }
        });
    }

    @Override
    public void initData() {

    }

    /**
	 * unlike a comment
	 * 
	 * @param commentId : comment id
	 * @param pos : position of comment
	 */
	protected void unLikeComment(String commentId,final int pos) {
        ((MainActivity) getActivity()).getFacebookHelper().unLike(commentId,new IActionLikeFacebook() {
            @Override
            public void onLikeFacebookSuccess() {
                if (mListView != null) {
                    View view = mListView.findViewWithTag(pos);
                    if (view != null) {
                        TextView txtThich = (TextView) view
                                .findViewById(R.id.txtThich);
                        TextView txtBoThich = (TextView) view
                                .findViewById(R.id.txtBoThich);
                        TextView txtLikeCOmmentCount = (TextView) view
                                .findViewById(R.id.txtLikeCOmmentCount);
                        txtThich.setVisibility(View.VISIBLE);
                        txtBoThich.setVisibility(View.GONE);
                        int count = (Integer) txtLikeCOmmentCount
                                .getTag();
                        txtLikeCOmmentCount.setText((count + 1) + "");
                    }
                }
            }

            @Override
            public void onLikeFacebookFail() {

            }
        });
	}

	/**
	 * like a comment
	 * 
	 * @param commentId : comment id
	 * @param pos : position of comment
	 */
	protected void likeComment(String commentId, final int pos) {
        ((MainActivity) getActivity()).getFacebookHelper().like(commentId, new IActionLikeFacebook() {
            @Override
            public void onLikeFacebookSuccess() {
                if (mListView != null) {
                    View view = mListView.findViewWithTag(pos);
                    if (view != null) {
                        TextView txtThich = (TextView) view
                                .findViewById(R.id.txtThich);
                        TextView txtBoThich = (TextView) view
                                .findViewById(R.id.txtBoThich);
                        TextView txtLikeCOmmentCount = (TextView) view
                                .findViewById(R.id.txtLikeCOmmentCount);
                        txtThich.setVisibility(View.GONE);
                        txtBoThich.setVisibility(View.VISIBLE);
                        int count = (Integer) txtLikeCOmmentCount
                                .getTag();
                        txtLikeCOmmentCount.setText((count + 1) + "");
                    }
                }
            }

            @Override
            public void onLikeFacebookFail() {

            }
        });
	}



	/**
	 * create a comment
	 * @param msg :message
	 */
	protected void postComment(final String msg) {
        ((MainActivity) getActivity()).getFacebookHelper().postComment(photoId, msg, new IPostCommentFacebook() {
            @Override
            public void onPostCommentFacebookSuccess(String postId) {
                if (((MainActivity) getActivity()).getFacebookHelper().getActiveUserFacebook() != null) {
                    edittextComment.setText("");
                    Comment comment = new Comment();
                    comment.setUser_likes(false);
                    comment.setMessage(msg);
                    long unixTime = System.currentTimeMillis() / 1000L;
                    comment.setCreated_time(unixTime + "");
                    comment.setId(postId);
                    User user = new User();
                    user.setId(((MainActivity) getActivity()).getFacebookHelper().getActiveUserFacebook().getId() + "");
                    user.setName(((MainActivity) getActivity()).getFacebookHelper().getActiveUserFacebook().getName() + "");
                    comment.setUser(user);
                    comments.add(comment);
                    bindData();

                    ((MainActivity) getActivity()).updateCommentPhotoObject(
                            postionOfPhoto);
                }
            }

            @Override
            public void onPostCommentFacebookFail() {

            }
        });
	}

	/**
	 * like a photo
	 */
	protected void likePhoto() {
        ((MainActivity) getActivity()).getFacebookHelper().like(photoId, new IActionLikeFacebook() {
            @Override
            public void onLikeFacebookSuccess() {
                ((MainActivity) getActivity()).updatePhotoObject(
                        postionOfPhoto, true);
                Toast.makeText(getActivity(),
                        getString(R.string.likesuccess),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLikeFacebookFail() {
                Toast.makeText(getActivity(),
                        getString(R.string.likefail),
                        Toast.LENGTH_SHORT).show();
            }
        });
	}

	/**
	 * show message confirm user set wall paper
	 */
	protected void showMessageConfirmSetWallPapper() {
//		DialogUtil.showMessageDialog(getActivity(),
//				R.string.wallpaper, R.string.confirm_set_wall_paper,
//				R.string.Ok, new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						setWallPaper();
//					}
//				}, R.string.close, new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//
//					}
//				});
	}

	/**
	 * set wall paper
	 */
	protected void setWallPaper() {
		if (source != null && !source.equals(""))
			ImageLoader.getInstance().loadImage(source,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);
							WallpaperManager myWallpaperManager = WallpaperManager
									.getInstance(getActivity()
											.getApplicationContext());
							myWallpaperManager.suggestDesiredDimensions(
									CommonUtils.getWidthScreen(getActivity()
                                            .getApplicationContext()),
									CommonUtils.getHeightScreen(getActivity()
											.getApplicationContext()));
							try {
								myWallpaperManager.setBitmap(loadedImage);
								Toast.makeText(
										getActivity(),
										getString(R.string.setwallpapersuccess),
										Toast.LENGTH_SHORT).show();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
	}

	/**
	 * process response of more comment
	 * 
	 * @param response : : response to process
	 * @throws JSONException
	 */
	protected void processResponse(Response response) throws JSONException {
		GraphObject graphObject = response.getGraphObject();
		if (graphObject != null) {
			JSONObject grap = graphObject.getInnerJSONObject();
			JSONArray array = grap.getJSONArray("data");
            DebugLog.d(
                    "comment size " + comments.size() + " data size "
							+ array.length());
			if (array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					Comment comment = new Comment();
					String fromId = object.getString("fromid");
					String text = object.getString("text");
					String time = object.getString("time");
					String object_id = object.getString("object_id");
					int likes = object.getInt("likes");
					comment.setCreated_time(time);
					comment.setLike_count(likes);
					comment.setId(object_id);
					comment.setMessage(text);
					Log.e("", "object " + object.toString());
					User user = new User();
					user.setId(fromId);
					comment.setUser(user);
					comments.add(comment);
				}
				loadUserComment(array.length());
			} else {
				loadmorecommentll.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * load user for comments
	 * 
	 * @param size
	 *            : size of new comment
	 */
	private void loadUserComment(int size) {
		if (CommonUtils.checkNetworkAvaliable(getActivity()
				.getApplicationContext())) {
            DebugLog.d(  "photo id " + photoId);
			String fqlquery = "select name from user where uid IN (";
			for (int i = commentOffset; i < commentOffset + size; i++) {
				if (i == commentOffset + size - 1) {
					fqlquery += comments.get(i).getUser().getId() + ")";
				} else {
					fqlquery += comments.get(i).getUser().getId() + ",";
				}
			}
			Bundle bundle = new Bundle();
			bundle.putString("q", fqlquery);

			Request request = new Request(null, "/fql", bundle, HttpMethod.GET,
					new Callback() {

						@Override
						public void onCompleted(Response response) {
							try {
								processUserResponse(response);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
			RequestAsyncTask asyncTask = new RequestAsyncTask(request);
			asyncTask.execute();
		} else {
			loadingText.setText(getString(R.string.loaderror));
		}
	}

	/**
	 * process user response
	 * 
	 * @param response : response to process
	 * @throws JSONException
	 */
	protected void processUserResponse(Response response) throws JSONException {
		GraphObject graphObject = response.getGraphObject();
		if (graphObject != null) {
			JSONObject grap = graphObject.getInnerJSONObject();
			JSONArray array = grap.getJSONArray("data");
			if (array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					String name = object.getString("name");
					comments.get(commentOffset + i).getUser().setName(name);
				}
				commentOffset += array.length();
				bindData();
			}
		}
	}

	/**
	 * bind comment info to view
	 * 
	 * @param photo : the photo data
	 * @param postionOfPhoto : position of photo
	 */
	public void bindCommentInfo(Photo photo, int postionOfPhoto) {
		likeFbTextView.setText(photo.getLike_count() + "");
		commentCount = photo.getComment_count();
		commentFbTextView.setText(commentCount + "");
		String photo_id = photo.getId();
		if(photo_id==null){
			photoId = "";
			loadComment = true;
			source = "";
			this.postionOfPhoto = postionOfPhoto;
			linkPhoto = "";
			loadComment = true;
			commentOffset = 0;
			comments.clear();
		}else{
			if (!photo_id.equals(photoId)) {
				photoId = photo_id;
				source = photo.getSource();
				this.postionOfPhoto = postionOfPhoto;
				linkPhoto = photo.getLink();
				loadComment = true;
				commentOffset = 0;
				comments.clear();
			} else {
				loadComment = false;
			}
		}
	}

	/**
	 * load current comment to view
	 */
	public void loadCommentToView() {
        DebugLog.d(  "loadComment " + loadComment);
		if (loadComment) {
			loadCommentLL.setVisibility(View.VISIBLE);
			loadingText.setText(getString(R.string.loading));
			mListView.setVisibility(View.INVISIBLE);
			loadCommen();
		} else {
			loadCommentLL.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * send request load comment Fb
	 */
	private void loadCommen() {
		loadComment = false;
		if (CommonUtils.checkNetworkAvaliable(getActivity()
				.getApplicationContext())) {
            DebugLog.d(  "photo id " + photoId);
			String fqlQuery = "SELECT fromid, text, time, object_id,time,likes FROM comment where object_id='"
					+ photoId
					+ "'  order by time desc limit 25 offset "
					+ commentOffset;
			Bundle bundle = new Bundle();
			bundle.putString("q", fqlQuery);

			Request request = new Request(null, "/fql", bundle, HttpMethod.GET,
					new Callback() {

						@Override
						public void onCompleted(Response response) {
							try {
								processResponse(response);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
			RequestAsyncTask asyncTask = new RequestAsyncTask(request);
			asyncTask.execute();
		} else {
			loadingText.setText(getString(R.string.loaderror));
		}
	}

	/**
	 * create list view data
	 * 
	 */
	private void bindData() {
		Collections.sort(comments, new TimeCommentComparator());
		if (mAdapter == null) {
			mAdapter = new CommentAdapter(
					getActivity().getApplicationContext(),
					likeCommentListenner, unlikeCommentListenner);
			mListView.setAdapter(mAdapter);
		}
		mAdapter.setData(comments);
		loadCommentLL.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		if (comments.size() < commentCount) {
			loadmorecommentll.setVisibility(View.VISIBLE);
		} else {
			loadmorecommentll.setVisibility(View.GONE);
		}
		loadmorecommentprogress.setVisibility(View.GONE);
		loadingmorecommenttxt.setText(getString(R.string.loadpreveouscomment));
	}

}
