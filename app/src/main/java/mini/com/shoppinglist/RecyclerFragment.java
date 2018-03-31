package mini.com.shoppinglist;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static android.app.Activity.RESULT_OK;


public class RecyclerFragment extends Fragment {

    private List<Product> mProducts = new ArrayList<>();
    private RecyclerView mProductListRecyclerView;
    private RecyclerView.Adapter mProductListAdapter;
    private RecyclerView.LayoutManager mProductListLayoutManager;
    private FloatingActionButton mFab;
    private static final int REQ_CODE_NEW_PRODUCT = 1;
    private static final int REQ_CODE_EDIT_PRODUCT = 2;


    public RecyclerFragment() {
        // Required empty public constructor
    }

    public void onActivityResult (int requestCode, int resultCode, Intent data) {


        if(resultCode == RESULT_OK && requestCode == REQ_CODE_NEW_PRODUCT) {
            Product newProduct = (Product)data.getExtras().getSerializable("new_product");
            mProducts.add(newProduct);
            saveSharedPreferencesProductsList();
            mProductListAdapter.notifyDataSetChanged();
        }else if (resultCode == RESULT_OK && requestCode == REQ_CODE_EDIT_PRODUCT){
            Product edited = (Product)data.getExtras().getSerializable("edited_product");
            int position = data.getIntExtra("position", 0);
            mProducts.set(position, edited);
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

        mProductListAdapter = new ProductListAdapter(mProducts, this);
        mProductListRecyclerView.setAdapter(mProductListAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
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
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                String json = gson.toJson(mProducts);
                try {
                    FileOutputStream outputStream = getActivity()
                            .openFileOutput("productsList", Context.MODE_PRIVATE);
                    outputStream.write(json.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        r.run();
    }
    public List<Product> loadSharedPreferencesProductsList() {
        List<Product> productsList = new ArrayList<>();
        Gson gson = new Gson();

        try {
            FileInputStream inputStream = getActivity()
                    .openFileInput("productsList");
            String json = convertStreamToString(inputStream);

            if (!json.isEmpty()) {
                Type type = new TypeToken<List<Product>>() {}.getType();
                productsList = gson.fromJson(json, type);
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productsList;
    }
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
