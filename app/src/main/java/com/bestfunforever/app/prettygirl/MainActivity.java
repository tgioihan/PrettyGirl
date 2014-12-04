package com.bestfunforever.app.prettygirl;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.android.lib.core.util.DebugLog;
import com.bestfunforever.app.prettygirl.fragment.MainFragment;
import com.bestfunforever.app.prettygirl.fragment.MenuFragment;
import com.bestfunforever.app.prettygirl.model.Photo;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends MenuActionActivity {

	MainFragment mainFragment;
	MenuFragment menuFragment;
	private DrawerLayout mDrawerLayout;
    private FrameLayout frameLayout;
	private InterstitialAd interstitial;

    @Override
    protected void preOnCreate(Bundle savedInstanceState) {
        super.preOnCreate(savedInstanceState);
        DebugLog.setEnable(true);
        facebookHelper.getKeyHash();
    }

    @Override
    protected void initView() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg_black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        frameLayout = (FrameLayout) findViewById(R.id.menu_frame);
        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mainFragment).commit();
        menuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, menuFragment).commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.app_name,
                R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mainFragment.getTitle());
                getSupportActionBar().hide();
                mainFragment.changepageIfNeed();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().show();
                getActionBar().setTitle(getString(R.string.app_name));
                if (mainFragment != null) {
                    mainFragment.setBackCount(0);
                }
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void initData() {
        loadInterstitialAd();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.main_activity;
    }


    public void loadInterstitialAd() {
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-2714906120093430/6978388901");

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d("", "loadInterstitialAd onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                String message = String.format("onAdFailedToLoad (%s)", getErrorReason(errorCode));
                Log.d("", "loadInterstitialAd " + message);
            }
        });

        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);
    }

    public void loadData() {
		mainFragment.loadData();
		
	}

	public void bindCommentDataToView(Photo photo, int pos) {
		mainFragment.bindCommentDataToView(photo, pos);
	}

	public void setPage(int i) {
		mainFragment.setPage(i);
	}

	public void updateCommentPhotoObject(int postionOfPhoto) {
		mainFragment.updateCommentPhotoObject(postionOfPhoto);
	}

	public void updatePhotoObject(int postionOfPhoto, boolean likeSuccess) {
		mainFragment.updatePhotoObject(postionOfPhoto, likeSuccess);
	}

	public void toogleActionBar() {
		mainFragment.toogleActionBar();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("", "menuitem "+item.getItemId());
		if(item.getItemId() == R.id.home){
			mDrawerLayout.openDrawer(frameLayout);
			return true;
		}
        if (mainFragment != null && mainFragment.onOptionsItemSelected(item)) return false;
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onBackPressed() {
        if (mainFragment == null || mainFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

	public void setPositionPage(int arg2) {
		if (mainFragment != null) {
			mainFragment.setPosPage(arg2);
		}
	}

	public void toogleDrawer() {
		// TODO Auto-generated method stub
		if (mDrawerLayout.isDrawerOpen(frameLayout)) {
			mDrawerLayout.closeDrawers();
		} else {
			mDrawerLayout.openDrawer(frameLayout);
		}
	}
	

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		if (mainFragment != null) {
			mainFragment.onOptionsMenuClosed(menu);
		}
		super.onOptionsMenuClosed(menu);
	}

	private String getErrorReason(int errorCode) {
	    String errorReason = "";
	    switch(errorCode) {
	      case AdRequest.ERROR_CODE_INTERNAL_ERROR:
	        errorReason = "Internal error";
	        break;
	      case AdRequest.ERROR_CODE_INVALID_REQUEST:
	        errorReason = "Invalid request";
	        break;
	      case AdRequest.ERROR_CODE_NETWORK_ERROR:
	        errorReason = "Network Error";
	        break;
	      case AdRequest.ERROR_CODE_NO_FILL:
	        errorReason = "No fill";
	        break;
	    }
	    return errorReason;
	  }

	public void showInterstitialAd() {
		Log.d("", "showInterstitialAd "+interstitial.isLoaded());
		 if (interstitial.isLoaded()) {
		      interstitial.show();
		    }else{
		    	loadInterstitialAd();
		    }
	}

	public void setTitleActionbar(String title) {
		if (mainFragment != null) {
            getSupportActionBar().setTitle(title);
		}
	}

}
