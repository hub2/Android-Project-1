package mini.com.shoppinglist;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>   {
    private List<Product> mDataset;
    private final Fragment mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLinearLayout;
        public TextView mTextView;
        public Product product;
        public final Fragment mContext;
        public int position;
        private int REQ_CODE_EDIT_PRODUCT = 2;
        public ViewHolder(LinearLayout v, final Fragment context) {
            super(v);
            mLinearLayout = v;
            mContext = context;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent child = new Intent(context.getActivity(), EditProductActivity.class);
                    child.putExtra("product", product);
                    child.putExtra("position", position);

                    mContext.startActivityForResult(child, REQ_CODE_EDIT_PRODUCT);
                }
            });
            mTextView = (TextView) v.findViewById(R.id.recycler_text_view);
        }
    }

    public ProductListAdapter(List<Product> myDataset, Fragment context) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_cell, parent, false);

        return new ViewHolder(v, mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.product = mDataset.get(position);
        holder.position = position;
        holder.mTextView.setText(mDataset.get(position).Name);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void removeAt(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
    }
}
