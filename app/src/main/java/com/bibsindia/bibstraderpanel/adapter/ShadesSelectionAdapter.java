package com.bibsindia.bibstraderpanel.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.model.catalogue.ShadeDetail;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ShadesSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ShadeDetail> mListData;
    private Activity mActivity;
    private boolean isShared;
    private onClickListener mListener;

    public ShadesSelectionAdapter(Activity mActivity, ArrayList<ShadeDetail> mListData, boolean isShared, onClickListener mListener) {
        this.mActivity = mActivity;
        this.isShared = isShared;
        this.mListData = mListData;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (isShared)
            return new ViewHolder(inflater.inflate(R.layout.item_shades_selection_share, parent, false));
        else
            return new ViewHolder(inflater.inflate(R.layout.item_shades_selection, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        ShadeDetail mShadeDetail = mListData.get(position);

        Log.w("ShadeDetails", ">>" + new Gson().toJson(mShadeDetail));

        Glide.with(mActivity).load(mShadeDetail.getShadeThumbImage()).into(itemHolder.imgShades);
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

        itemHolder.rootShades.setOnClickListener(v -> {
            mListener.onShadeClick(mShadeDetail);
        });

        if (!isShared) {
            itemHolder.edQty.setText(String.valueOf(mShadeDetail.getTotalQty()));
            itemHolder.checkShades.setChecked(mShadeDetail.isChecked());

            itemHolder.imgMinus.setOnClickListener(v -> {
                int current = Integer.parseInt(String.valueOf(itemHolder.edQty.getText()));
                if (current > 0)
                    current--;
                itemHolder.edQty.setText(String.valueOf(current));
                mShadeDetail.setTotalQty(current);
            });


            /*itemHolder.edQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString();
                    if (str.isEmpty()) {
                        mShadeDetail.setTotalQty(0);
                        return;
                    }

                    int current = Integer.parseInt(str);
                    Log.w("ShadeQty", "Enter : " + current);
                    mShadeDetail.setTotalQty(current);
                }
            });*/


            itemHolder.imgPlus.setOnClickListener(v -> {
                int current = Integer.parseInt(String.valueOf(itemHolder.edQty.getText()));
                current++;
                itemHolder.edQty.setText(String.valueOf(current));
                mShadeDetail.setTotalQty(current);
            });

            itemHolder.checkShades.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mShadeDetail.setChecked(isChecked);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout rootShades;
        public LinearLayout rootLayout;
        public AppCompatCheckBox checkShades;
        public AppCompatImageView imgShades;
        public AppCompatTextView txtShadesNo;
        public AppCompatTextView txtShadesName;
        public AppCompatImageView imgMinus;
        public AppCompatImageView imgPlus;
        public AppCompatEditText edQty;
        public LinearLayout layoutBucket;

        public ViewHolder(View v) {
            super(v);
            if (isShared) {
                rootShades = v.findViewById(R.id.rootShades);
                imgShades = v.findViewById(R.id.imgShades);
                txtShadesName = v.findViewById(R.id.txtShadesName);
                txtShadesNo = v.findViewById(R.id.txtShadesNo);
                txtShadesName.setSelected(true);
                return;
            }

            rootLayout = v.findViewById(R.id.rootLayout);
            checkShades = v.findViewById(R.id.checkShades);
            rootShades = v.findViewById(R.id.rootShades);
            imgShades = v.findViewById(R.id.imgShades);
            txtShadesNo = v.findViewById(R.id.txtShadesNo);
            txtShadesName = v.findViewById(R.id.txtShadesName);
            imgMinus = v.findViewById(R.id.imgMinus);
            imgPlus = v.findViewById(R.id.imgPlus);
            edQty = v.findViewById(R.id.edQty);
            layoutBucket = v.findViewById(R.id.layoutBucket);
            txtShadesName.setSelected(true);
        }
    }

    public void SetQty(int qty) {
        int i = 0;
        for (ShadeDetail mDetails : mListData) {
            if (mDetails.isChecked()) {
                mDetails.setTotalQty(qty);
                notifyItemChanged(i);
            }
            i++;
        }
    }

    public void CheckAll() {
        int i = 0;
        for (ShadeDetail mDetails : mListData) {
            mDetails.setChecked(true);
            notifyItemChanged(i);
            i++;
        }
    }

    public void UncheckAll() {
        int i = 0;
        for (ShadeDetail mDetails : mListData) {
            mDetails.setChecked(false);
            notifyItemChanged(i);
            i++;
        }
    }

    public ArrayList<ShadeDetail> GetData() {
        return mListData;
    }

    public interface onClickListener {
        void onShadeClick(ShadeDetail mShadeDetail);
    }
}