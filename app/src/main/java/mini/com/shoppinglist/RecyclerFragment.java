package mini.com.shoppinglist;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class RecyclerFragment extends Fragment {

    private List<Product> mProducts = new ArrayList<>();
    private RecyclerView mProductListRecyclerView;
    private RecyclerView.Adapter mProductListAdapter;
    private RecyclerView.LayoutManager mProductListLayoutManager;
    private FloatingActionButton mFab;
    private static final int REQ_CODE_NEW_PRODUCT = 1;


    public RecyclerFragment() {
        // Required empty public constructor
    }

    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQ_CODE_NEW_PRODUCT) {
            Product newProduct = (Product)data.getExtras().getSerializable("new_product");
            mProducts.add(newProduct);
            saveSharedPreferencesProductsList();
            mProductListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        mProducts = loadSharedPreferencesProductsList();

        mProductListRecyclerView = view.findViewById(R.id.product_list_recycler_view);
        mFab = view.findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
            }
        });

        mProductListRecyclerView.setHasFixedSize(true);

        mProductListLayoutManager = new LinearLayoutManager(getActivity());
        mProductListRecyclerView.setLayoutManager(mProductListLayoutManager);

        mProductListAdapter = new ProductListAdapter(mProducts);
        mProductListRecyclerView.setAdapter(mProductListAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int id = viewHolder.getAdapterPosition();
                ((ProductListAdapter)mProductListAdapter).removeAt(id);
                saveSharedPreferencesProductsList();
                mProductListAdapter.notifyDataSetChanged();
            }
        });

        itemTouchHelper.attachToRecyclerView(mProductListRecyclerView);
        return view;
    }
    private void onFabClick(){
        Intent child = new Intent(getActivity(), AddProductActivity.class);
        startActivityForResult(child, REQ_CODE_NEW_PRODUCT);
    }
    public void saveSharedPreferencesProductsList(){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mProducts);
        prefsEditor.putString("productsList", json);
        prefsEditor.apply();
    }
    public List<Product> loadSharedPreferencesProductsList() {
        List<Product> productsList;
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = mPrefs.getString("productsList", "");
        if (json.isEmpty()) {
            productsList = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<Product>>() {}.getType();
            productsList = gson.fromJson(json, type);
        }
        return productsList;
    }
}
