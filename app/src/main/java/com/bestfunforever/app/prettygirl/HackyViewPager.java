package com.bestfunforever.app.prettygirl;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * 
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * 
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 * 
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {

	private boolean isPagingEnabled=true;

	public HackyViewPager(Context context) {
		super(context);
	}

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

    @Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(isPagingEnabled){
			try {
				Log.e("", "onInterceptTouchEvent super");
				return super.onInterceptTouchEvent(ev);
				
			}
			catch (ArrayIndexOutOfBoundsException e) {
				// TODO: handle exception
				Log.e("", "onInterceptTouchEvent "+e.toString());
				return false;
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				Log.e("", "onInterceptTouchEvent "+e.toString());
				return false;
			}
		}else{
			return false;
		}
	}
	

}
