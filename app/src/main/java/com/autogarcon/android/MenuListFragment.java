package com.autogarcon.android;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuListFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<Menu> menuList;
    private MenuListAdapter mAdapter;
    private String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.menuList = (ArrayList<Menu>)(getActivity().getIntent()).getSerializableExtra("menuList");
        recyclerView = (RecyclerView) (getActivity()).findViewById(R.id.recycler_view);
        mAdapter = new MenuListAdapter(menuList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (menuList.get(position).getCategories().size() == 1){
                    Intent intent = new Intent(getContext(), MenuItemListActivity.class);
                    intent.putExtra("category", menuList.get(position).getCategories().get(0));
                    intent.putExtra("title", title);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else {
                    Intent intent = new Intent(getContext(), CategoryListActivity.class);
                    intent.putExtra("menu", menuList.get(position));
                    intent.putExtra("title", title);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }
}
