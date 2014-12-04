package com.bestfunforever.app.prettygirl.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.core.slidepanel.SlideContainer;
import com.android.lib.core.activity.facebook.listener.ILoginFacebook;
import com.android.lib.core.fragment.BaseFragment;
import com.bestfunforever.app.prettygirl.Config;
import com.bestfunforever.app.prettygirl.MainActivity;
import com.bestfunforever.app.prettygirl.R;
import com.bestfunforever.app.prettygirl.model.Photo;
import com.bestfunforever.app.prettygirl.util.CommonUtils;
import com.bestfunforever.app.prettygirl.util.TimePhotoComparator;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Nguyen Xuan Tuan
 * 
 */
public class MainFragment extends BaseFragment {

	private int posPage;

	private LinearLayout mLoadingBg;
	private SlideContainer slidingView;

	private AdView mAdView;

	private int mPageOffset ;
	private ArrayList<Photo> photos = new ArrayList<Photo>();

	// gia tri size cac object trong mang du lieu tra ve
	private int mObjectCounts ;
	private int oldPhotoSize;
	
	private String[] pageNames;


    @Override
    protected void preOncreateView() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.main_fragment;
    }

    /**
     * init view
     */
    @Override
    protected void initView(View view) {
        mAdView = (AdView) view.findViewById(R.id.adView);

        initSlideContainer(view);

        mLoadingBg = (LinearLayout) view.findViewById(R.id.imgloading);


    }

    private void initSlideContainer(View view) {
        slidingView = (SlideContainer) view.findViewById(R.id.slide);
        slidingView.setContent(R.layout.frame1);
        slidingView.setBottomView(R.layout.frame2);
        slidingView.setBottomOffset(getResources().getDimensionPixelSize(R.dimen.bottom_offset));
        slidingView.setSlideChangeListener(new SlideContainer.ISlideChange() {
            @Override
            public void onStartSlide(boolean b) {

            }

            @Override
            public void onSlide(int i, boolean b) {

            }

            @Override
            public void onSlideFinish(boolean bottomIn) {
                if (bottomIn) {
                    Log.e("", "comeent frag display");
                    CommentFragment commentFragment = (CommentFragment) getActivity().getSupportFragmentManager()
                            .findFragmentById(R.id.frame2);
                    if (commentFragment != null) {
                        commentFragment.loadCommentToView();
                    }
                    backCount = 0;
                }
            }
        });
        PhotosFragment mainFragment = new PhotosFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame1, mainFragment).commit();

        CommentFragment commentFragment = new CommentFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame2, commentFragment).commit();
        Log.d(""   , " initSlideContainer ");
    }

    @Override
    public void initData() {
        if (CommonUtils.checkNetworkAvaliable(getActivity())) {
            if (Session.getActiveSession() != null && Session.getActiveSession().isOpened()) {
                loadData();
            } else {
                ((MainActivity) getActivity()).getFacebookHelper().loginFacebook(null,null,new ILoginFacebook() {
                    @Override
                    public void onLoginFacebookSuccess() {
                        loadData();
                    }

                    @Override
                    public void onLoginFacebookFail(Session session, SessionState sessionState, Exception e) {
                        showMessageDialog(R.string.error_connection, R.string.error_connection_msg);
                    }
                });
            }

        } else {
            showMessageDialog(R.string.error_connection, R.string.error_connection_msg);
        }
    }

    @Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(getActivity()).activityStart(getActivity()); // Add
																				// this
																				// method.
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(getActivity()).activityStop(getActivity()); // Add
																			// this
																			// method.
	}

	/**
	 * back count to quit app
	 */
	private int backCount;

	public boolean onBackPressed() {
		if (slidingView.isBottomIn()) {
			slidingView.setBotomIn(true);
			return false;
		} else if (backCount < 2) {
			backCount++;
			Toast.makeText(getActivity(), getString(R.string.backclick), Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
			
	}

	/**
	 * init page offset for facebook load data
	 */
	private void initPageOffsets() {
			mPageOffset = 0;
	}

	/**
	 * @param title
	 *            : title of dialog
	 * @param msg
	 *            : message of dialog
	 */
	public void showMessageDialog(int title, int msg) {
//		DialogUtil.showMessageDialog(getActivity(), title, msg, getString(R.string.Ok), new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//
//			}
//		});
	}
	
	private String title = "";

	private void setTitle() {
		title = pageNames[posPage];
	}
	
	public String getTitle(){
		return title;
	}

	/**
	 * load data from facebook
	 */
	public void loadData() {
		setTitle();
		showActionBarTItle();
		if (CommonUtils.checkNetworkAvaliable(getActivity())) {
			String fqlQuery = "{";
			fqlQuery = fqlQuery.concat("'photos" + Config.arr[posPage] + "':");
			fqlQuery = fqlQuery
					.concat("'select caption,images,created,object_id,link,created,album_object_id from photo where album_object_id="
							+ Config.arr_id[posPage] + " limit 25 offset " + mPageOffset + "',");
			fqlQuery = fqlQuery.concat("'commentlikeinfo" + Config.arr[posPage] + "':");
			fqlQuery = fqlQuery.concat("'select like_info, comment_info from photo where album_object_id="
					+ Config.arr_id[posPage] + " limit 25 offset " + mPageOffset + "',");
			fqlQuery = fqlQuery.concat("}");
			Log.e("", "fqlquery " + fqlQuery);
			Bundle params = new Bundle();
			params.putString("q", fqlQuery);
			Request request = new Request(Session.getActiveSession(), "/fql", params, HttpMethod.GET,
					new Request.Callback() {

						@Override
						public void onCompleted(Response response) {
							initListPhoto(response);
						}


					});
			RequestAsyncTask asyncTask = new RequestAsyncTask(request);
			asyncTask.execute();
		} else
			showMessageDialog(R.string.error_connection, R.string.error_connection_msg);
	}

	private void showActionBarTItle() {
		((MainActivity)getActivity()).setTitleActionbar(title);
	}

	/**
	 * init photos data from facebook api response
	 * 
	 * @param response
	 *            : response from facebook api
	 */
	protected void initListPhoto(Response response) {
		GraphObject graphObject = response.getGraphObject();
		Log.d("", "initListPhoto rres " + response.toString());
		if (graphObject != null) {
			JSONObject grap = graphObject.getInnerJSONObject();
			if (grap != null) {
				try {
					JSONArray array = grap.getJSONArray("data");
					if (array.length() > 0) {
						initCachePhotos(array);

						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.getJSONObject(i);
							String nameobject = object.getString("name");
							JSONArray fqlresult = object.getJSONArray("fql_result_set");

							// kiem tra name object tuong ung voi mang cac page
							for (int j = 0; j < Config.arr.length; j++) {
								if (nameobject.equals("photos" + Config.arr[j])) {
									bindImageDataForPhoto(oldPhotoSize, mObjectCounts, fqlresult);
								} else if (nameobject.equals("commentlikeinfo" + Config.arr[j])) {
									bindCommentDataForPhoto(oldPhotoSize, mObjectCounts, fqlresult);
								}
							}
						}

						indexOffset(array);
						if (photos.size() == 0) {
//							DialogUtil.showMessageDialog(getActivity(), getString(R.string.error_loaddata_title),
//									getString(R.string.error_loaddata_msg), getString(R.string.Ok),
//									new OnClickListener() {
//
//										@Override
//										public void onClick(View arg0) {
//										}
//									});
						} else {
							Collections.sort(photos, new TimePhotoComparator());
							Log.e("", "photo size " + photos.size());
							PhotosFragment mainFragment = (PhotosFragment) getActivity().getSupportFragmentManager()
									.findFragmentById(R.id.frame1);
							mainFragment.bindData(photos,needChange);
							if (mLoadingBg.getVisibility() == View.VISIBLE) {
								Animation animation_left_right = AnimationUtils.loadAnimation(getActivity(),
										R.anim.slide_right_to_left);
								mLoadingBg.startAnimation(animation_left_right);
								mLoadingBg.setVisibility(View.GONE);
							}
						}
						handler.removeCallbacks(hideActionBarRunnaable);
						handler.postDelayed(hideActionBarRunnaable, 2000);
						loadAds();
						needChange = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} else {

		}
	}

	/**
	 * load admod
	 */
	private void loadAds() {
		com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
	}

	/**
	 * reindes page offset list
	 * 
	 * @param array
	 *            : json array of data
	 */
	private void indexOffset(JSONArray array) {
		try {
				int tmp = array.getJSONObject(0).getJSONArray("fql_result_set").length();
				mPageOffset += tmp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * bind comment data for photos
	 * 
	 * @param oldPhotoSize
	 *            : old value of photos's size
	 * @param pos
	 *            : postion of photo need to set
	 * @param fqlresult
	 * @throws JSONException
	 */
	private void bindCommentDataForPhoto(int oldPhotoSize, int pos, JSONArray fqlresult) throws JSONException {
		for (int i = 0; i < fqlresult.length(); i++) {
			JSONObject object1 = fqlresult.getJSONObject(i);
			JSONObject like_info = object1.getJSONObject("like_info");
			boolean user_like = like_info.getBoolean("user_likes");
			int tmp = i + pos + oldPhotoSize;
			photos.get(tmp).setIs_user_like(user_like);
			int like_count = like_info.getInt("like_count");
			JSONObject comment_info = object1.getJSONObject("comment_info");
			int comment_count = comment_info.getInt("comment_count");
			photos.get(tmp).setLike_count(like_count);
			photos.get(tmp).setComment_count(comment_count);
		}
	}

	/**
	 * bind image data for photos
	 * 
	 * @param oldPhotoSize
	 *            : old value of photos's size
	 * @param pos
	 *            : postion of photo need to set
	 * @param fqlresult
	 * @throws JSONException
	 */
	private void bindImageDataForPhoto(int oldPhotoSize, int pos, JSONArray fqlresult) throws JSONException {
		int temp_photo_count = fqlresult.length();
		for (int i = 0; i < temp_photo_count; i++) {
			JSONObject object1 = fqlresult.getJSONObject(i);
			JSONArray imgArray = object1.getJSONArray("images");
			String source = "";
			if (imgArray.length() > 0) {
				JSONObject mJson = imgArray.getJSONObject(0);
				source = mJson.getString("source");
			}
			String link = object1.getString("link");

			String caption = object1.getString("caption");
			String photo_id = object1.getString("object_id");

			int tmp = i + pos + oldPhotoSize;
			photos.get(tmp).setLink(link);
			photos.get(tmp).setId(String.valueOf(photo_id));
			photos.get(tmp).setName(caption);
			photos.get(tmp).setSource(source);
			int created = object1.getInt("created");
			photos.get(tmp).setCreated(created);
			String album_id = object1.getString("album_object_id");
			photos.get(tmp).setAlbum_object_id(album_id);

		}
	}

	/**
	 * init list cache photo for new data
	 * 
	 * @param array
	 *            : json array of data
	 */
	private void initCachePhotos(JSONArray array) {
		int objectCount = 0;
		try {
			int objectCountTmp = 0;
				int tmp = array.getJSONObject(0).getJSONArray("fql_result_set").length();
				Log.d("", "initCachePhotos array size "+ tmp);
				objectCount += tmp;
				Log.d("", "initCachePhotos objectCount "+objectCount);
				mObjectCounts = objectCountTmp;
				objectCountTmp += tmp;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("", "initCachePhotos ");
		if (objectCount > 0) {
			int tmp = 0;
			if (photos.size() == 0) {
				oldPhotoSize = 0;
				tmp = objectCount;
				Log.d("", "initCachePhotos tmp "+tmp);
			} else {
				tmp = objectCount - 1;
				oldPhotoSize = photos.size() - 1;
				Log.d("", "initCachePhotos ");
			}
			Log.d("", "initCachePhotos tmp "+tmp+" photos size "+ photos.size());
			for (int i = 0; i < tmp; i++) {
				Photo photo = new Photo();
				photos.add(photo);
			}
		}
	}



	/**
	 * bind comment data to CommentFragment
	 * 
	 * @param photo
	 *            : photo data
	 * @param pos
	 *            : postion of photo
	 */
	public void bindCommentDataToView(Photo photo, int pos) {
		CommentFragment commentFragment = (CommentFragment) getActivity().getSupportFragmentManager().findFragmentById(
				R.id.frame2);
		if (commentFragment != null) {
			commentFragment.bindCommentInfo(photo, pos);
		}
		if (pos != 0 && pos % 25 == 0) {
			// Random random = new Random();
			// int tmp = random.nextInt(1);
			// if(tmp == 0 ){
			// showMobileCoreAds(null);
			// }
			((MainActivity) getActivity()).showInterstitialAd();
		}
	}

	/**
	 * set page display of sliding view
	 * 
	 * @param i
	 */
	public void setPage(int i) {
		boolean bottomIn = slidingView.isBottomIn();
		if (i == 1) {
			if (!bottomIn) {
				slidingView.setBotomIn(true);
			}
		} else {
			if (bottomIn) {
                slidingView.setBotomIn(false);
			}
		}
	}

	/**
	 * @param postionOfPhoto
	 * @param likeSuccess
	 */
	public void updatePhotoObject(int postionOfPhoto, boolean likeSuccess) {
		photos.get(postionOfPhoto).setIs_user_like(likeSuccess);
		photos.get(postionOfPhoto).setLike_count(photos.get(postionOfPhoto).getLike_count() + 1);
		PhotosFragment mainFragment = (PhotosFragment) getActivity().getSupportFragmentManager().findFragmentById(
				R.id.frame1);
		if (mainFragment != null) {
			int tmp = mainFragment.getCurrentPage();
			if (postionOfPhoto == tmp) {
				bindCommentDataToView(photos.get(postionOfPhoto), postionOfPhoto);
			}
		}
	}

	public int getBackCount() {
		return backCount;
	}

	public void setBackCount(int backCount) {
		this.backCount = backCount;
	}

	Handler handler = new Handler();

	Runnable hideActionBarRunnaable = new Runnable() {

		@Override
		public void run() {
			if (((MainActivity) getActivity()).getSupportActionBar() != null
					&& ((MainActivity) getActivity()).getSupportActionBar().isShowing()) {
                ((MainActivity) getActivity()).getSupportActionBar().hide();
			}
		}
	};

	/**
	 * show or hide action using delay time
	 */
	public void toogleActionBar() {
		if (((MainActivity) getActivity()).getSupportActionBar().isShowing()) {
			handler.removeCallbacks(hideActionBarRunnaable);
			handler.postDelayed(hideActionBarRunnaable, 2000);
		} else {
            ((MainActivity) getActivity()).getSupportActionBar().show();
			handler.postDelayed(hideActionBarRunnaable, 2000);
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.show_menu) {
			handler.removeCallbacks(hideActionBarRunnaable);
			return true;
		}
		return false;
	}

	public void onOptionsMenuClosed(Menu menu) {
		handler.removeCallbacks(hideActionBarRunnaable);
		handler.postDelayed(hideActionBarRunnaable, 2000);
	}

	/**
	 * @param postionOfPhoto
	 */
	public void updateCommentPhotoObject(int postionOfPhoto) {
		photos.get(postionOfPhoto).setComment_count(photos.get(postionOfPhoto).getComment_count() + 1);
		PhotosFragment mainFragment = (PhotosFragment) getActivity().getSupportFragmentManager().findFragmentById(
				R.id.frame1);
		if (mainFragment != null) {
			int tmp = mainFragment.getCurrentPage();
			if (postionOfPhoto == tmp) {
				bindCommentDataToView(photos.get(postionOfPhoto), postionOfPhoto);
			}
		}
	}

	public int getPosPage() {
		return posPage;
	}

	private boolean needChange = false;

	public void setPosPage(int posPage) {
		if (posPage != this.posPage) {
			this.posPage = posPage;
			needChange = true;
		} else {
			needChange = false;
		}

	}

	public void changepageIfNeed() {
		if (needChange) {
			showSplash();
			mPageOffset = 0;
			mObjectCounts = 0;
			photos.clear();
			loadData();
		}
	}
	
	public void showSplash(){
			Animation animation_left_right = AnimationUtils.loadAnimation(getActivity(),
					R.anim.slide_leff_to_right);
			mLoadingBg.startAnimation(animation_left_right);
			mLoadingBg.setVisibility(View.VISIBLE);
	}
}
