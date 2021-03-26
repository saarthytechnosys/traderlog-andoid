package com.bibsindia.bibstraderpanel.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.model.orderDetails.ShadeDetail;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShadesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ShadeDetail> mListData;
    private Activity mActivity;
    private onClickListener mListener;

    public ShadesAdapter(Activity mActivity, ArrayList<ShadeDetail> mListData, onClickListener mListener) {
        this.mActivity = mActivity;
        this.mListData = mListData;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_shades, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        ShadeDetail mShadeDetail = mListData.get(position);

        Glide.with(mActivity).load(mShadeDetail.getShadeThumbImage()).into(itemHolder.imgShades);

        itemHolder.txtTotalQty.setText(String.valueOf(mShadeDetail.getShadeQuantity()));

        String mShadeName = mShadeDetail.getShadeName();
        if (mShadeName == null || mShadeName.isEmpty())
            itemHolder.txtShadesName.setText("");
        else {
            itemHolder.txtShadesName.setText(mShadeName);
        }

        String mShadeNo = mShadeDetail.getShadeNo();
        if (mShadeNo == null || mShadeNo.isEmpty())
            itemHolder.txtShadesNo.setText("");
        else {
            itemHolder.txtShadesNo.setText(mShadeNo);
        }

        itemHolder.rootShades.setOnClickListener(v -> mListener.onShadeClick(mShadeDetail));
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout rootShades;
        CardView rootLayout;
        AppCompatImageView imgShades;
        AppCompatTextView txtTotalQty;
        AppCompatTextView txtShadesName;
        AppCompatTextView txtShadesNo;

        public ViewHolder(View v) {
            super(v);
            rootShades = v.findViewById(R.id.rootShades);
            rootLayout = v.findViewById(R.id.rootLayout);
            imgShades = v.findViewById(R.id.imgShades);
            txtTotalQty = v.findViewById(R.id.txtTotalQty);
            txtShadesName = v.findViewById(R.id.txtShadesName);
            txtShadesNo = v.findViewById(R.id.txtShadesNo);
            txtShadesName.setSelected(true);
        }
    }

    public interface onClickListener {
        void onShadeClick(ShadeDetail mShadeDetail);
    }
}