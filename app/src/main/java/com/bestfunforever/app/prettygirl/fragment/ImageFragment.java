package com.bestfunforever.app.prettygirl.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bestfunforever.app.prettygirl.MainActivity;
import com.bestfunforever.app.prettygirl.R;
import com.bestfunforever.app.prettygirl.photoview.PhotoView;
import com.bestfunforever.app.prettygirl.photoview.PhotoViewAttacher;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class ImageFragment extends Fragment {

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private PhotoView mImageView;
    private LinearLayout mProgessLayout;
    private ProgressBar mProgressBar;
    private TextView mProgressTextView;

    private String source = "";

    private int postion;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImageLoader();
    }

    private void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).cacheOnDisc(true).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.image, null);
        initView(view);

        loadData();
        return view;
    }

    /**
     * load image view
     */
    public void loadData() {
        if (!source.equals("")) {
            imageLoader.displayImage(source, mImageView, options, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    mProgressTextView.setText(getString(R.string.loaderror));
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mProgessLayout.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            }, new ImageLoadingProgressListener() {

                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    int percent = (int) (current / total * 100);
                    mProgressBar.setProgress(percent);
                    mProgressTextView.setText((int) (percent) + "%");
                }
            });
        } else {

        }
    }

    private void initView(View view) {
        mImageView = (PhotoView) view.findViewById(R.id.img);
        mProgessLayout = (LinearLayout) view.findViewById(R.id.progessll);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mProgressTextView = (TextView) view.findViewById(R.id.progressText);

        mImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                ((MainActivity) getActivity()).toogleActionBar();
            }
        });

    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

}
