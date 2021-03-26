package com.bibsindia.bibstraderpanel.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.model.order.Data;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Data> mListData;
    private Activity mActivity;
    private setOnItemClickListener sClickListener;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoadingAdded = false;

    public OrderAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        this.mListData = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                viewHolder = new ViewHolder(inflater.inflate(R.layout.item_history, parent, false));
                break;
            case VIEW_TYPE_LOADING:
                viewHolder = new LoadingViewHolder(inflater.inflate(R.layout.item_loading, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
//        return (position == mListData.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;

        Data mData = mListData.get(position);
        if (position == mListData.size() - 1 && isLoadingAdded)
            return VIEW_TYPE_LOADING;
        else {
            return VIEW_TYPE_ITEM;
        }
    }


    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM:
                ViewHolder itemHolder = (ViewHolder) holder;
                Data mData = mListData.get(position);

                Glide.with(mActivity).load(mData.getCatalogueThumbImage()).into(itemHolder.imgProduct);

                itemHolder.txtProductName.setText(mData.getProductName());
                itemHolder.txtOrderDate.setText(mData.getOrderDate());
                itemHolder.txtOrderNumber.setText(mData.getOrderNo());
                itemHolder.txtCatalogueName.setText(mData.getCatalogueName());
                itemHolder.txtTotalQty.setText(mData.getTotalQuantity());
                itemHolder.txtStatus.setText(mData.getAcknowledge().equals("0") ? "Pending" : "Approved");
                itemHolder.txtManufacturerName.setText(mData.getManufacturer_name());

                itemHolder.rootLayout.setOnClickListener(v -> sClickListener.onItemClick(mListData.get(position)));
                break;
            case VIEW_TYPE_LOADING:
                Log.w("LoadingView", "********************************************");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView rootLayout;
        AppCompatImageView imgProduct;
        AppCompatTextView txtProductName;
        AppCompatTextView txtOrderDate;
        AppCompatTextView txtOrderNumber;
        AppCompatTextView txtCatalogueName;
        AppCompatTextView txtTotalQty;
        AppCompatTextView txtStatus;
        AppCompatTextView txtManufacturerName;

        public ViewHolder(View v) {
            super(v);
            rootLayout = v.findViewById(R.id.rootLayout);
            imgProduct = v.findViewById(R.id.imgProduct);
            txtProductName = v.findViewById(R.id.txtProductName);
            txtOrderDate = v.findViewById(R.id.txtOrderDate);
            txtOrderNumber = v.findViewById(R.id.txtOrderNumber);
            txtCatalogueName = v.findViewById(R.id.txtCatalogueName);
            txtTotalQty = v.findViewById(R.id.txtTotalQty);
            txtStatus = v.findViewById(R.id.txtStatus);
            txtManufacturerName = v.findViewById(R.id.txtManufacturerName);
            txtManufacturerName.setSelected(true);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingViewHolder(View view) {
            super(view);
        }
    }

    public interface setOnItemClickListener {
        void onItemClick(Data mData);
    }

    public void setOnItemClickListener(setOnItemClickListener clickListener) {
        sClickListener = clickListener;
    }


    public void add(Data mData) {
        mListData.add(mData);
        notifyItemInserted(mListData.size() - 1);
    }

    public void add(Data mData, int position) {
        mListData.add(position, mData);
        notifyItemRangeChanged(position, mListData.size());
    }

    public void update(Data mData, int position) {
        try {
            mListData.set(position, mData);
            notifyItemChanged(position);
        } catch (IndexOutOfBoundsException pointerException) {
            Log.e("ListingAds", "Exception Error : IndexOutOfBoundsException");
            pointerException.printStackTrace();
        }
    }

    public void addAll(ArrayList<Data> mList) {
        for (Data mData : mList) {
            add(mData);
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Data());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mListData.size() - 1;
        Data item = getItem(position);

        if (item != null) {
            mListData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    private void remove(Data mData) {
        int position = mListData.indexOf(mData);
        if (position > -1) {
            mListData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Data getItem(int position) {
        return mListData.get(position);
    }
}