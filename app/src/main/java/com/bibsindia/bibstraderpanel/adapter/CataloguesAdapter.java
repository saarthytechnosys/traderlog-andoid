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
import com.bibsindia.bibstraderpanel.model.catalogue.Data;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CataloguesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Data> mListData;
    private Activity mActivity;
    private setOnItemClickListener sClickListener;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean isShared;

    public CataloguesAdapter(Activity mActivity, boolean isShared) {
        this.mActivity = mActivity;
        this.isShared = isShared;
        this.mListData = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                viewHolder = new ViewHolder(inflater.inflate(R.layout.item_catalogue, parent, false));
                break;
            case VIEW_TYPE_LOADING:
                viewHolder = new LoadingViewHolder(inflater.inflate(R.layout.item_loading, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
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

                Glide.with(mActivity).load(mData.getCatalogueThumbImage()).into(itemHolder.imgCatalogueImage);

                itemHolder.txtCatalogueName.setText(mData.getCatalogueName());
                itemHolder.txtCatalogueID.setText(String.format("(%s)", mData.getCatelogueNo()));

                if (!isShared)
                    itemHolder.txtPrice.setText(String.format(mActivity.getString(R.string.rupees), mData.getSalePrice()));

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
        AppCompatImageView imgCatalogueImage;
        AppCompatTextView txtCatalogueName;
        AppCompatTextView txtCatalogueID;
        AppCompatTextView txtPrice;

        public ViewHolder(View v) {
            super(v);
            rootLayout = v.findViewById(R.id.rootLayout);
            imgCatalogueImage = v.findViewById(R.id.imgCatalogueImage);
            txtCatalogueName = v.findViewById(R.id.txtCatalogueName);
            txtCatalogueID = v.findViewById(R.id.txtCatalogueID);
            txtPrice = v.findViewById(R.id.txtPrice);

            txtCatalogueName.setSelected(true);

            if (isShared)
                txtPrice.setVisibility(View.GONE);
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