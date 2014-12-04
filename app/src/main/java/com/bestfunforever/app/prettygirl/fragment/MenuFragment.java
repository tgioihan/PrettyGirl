package com.bestfunforever.app.prettygirl.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.lib.core.fragment.BaseFragment;
import com.bestfunforever.app.prettygirl.MainActivity;
import com.bestfunforever.app.prettygirl.R;

public class MenuFragment extends BaseFragment{

    @Override
    protected void preOncreateView() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.menu_fragment;
    }

    @Override
    protected void initView(View view) {
        ListView mListView = (ListView) view.findViewById(R.id.menulist);
        String[] menuStrings = getResources().getStringArray(R.array.pages);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, menuStrings);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ((MainActivity)getActivity()).setPositionPage(arg2);
                ((MainActivity)getActivity()).toogleDrawer();
            }
        });
    }

    @Override
    public void initData() {

    }

}
