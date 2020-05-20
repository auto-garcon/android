package com.autogarcon.android;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.autogarcon.android.API.APIUtils;
import com.autogarcon.android.API.Menu;
import com.autogarcon.android.API.MenuItem;

/**
 * Splits the MenuList into a fragment.
 * @author Tim Callies
 */
public class MenuListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Menu> menuList;
    private MenuListAdapter mAdapter;

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
                Menu menu = menuList.get(position);
                List<Map.Entry<String, List<MenuItem>>> categories = APIUtils.getCategories(menu);

                if (categories.size() == 1){
                    Intent intent = new Intent(getContext(), MenuItemListActivity.class);
                    intent.putExtra("category", (Serializable) menu.getMenuItems());
                    intent.putExtra("title", menu.getMenuName());
                    startActivityForResult(intent, 2);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else {
                    Intent intent = new Intent(getContext(), CategoryListActivity.class);
                    intent.putExtra("menu", (Serializable) categories);
                    intent.putExtra("title", menu.getMenuName());
                    startActivityForResult(intent, 2);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 4){
            openFragment(new ReceiptFragment());
        }
    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction f = getActivity().getSupportFragmentManager().beginTransaction();
        f.replace(R.id.top_frame, fragment);
        f.commit();
    }
}
