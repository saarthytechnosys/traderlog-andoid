package com.bibsindia.bibstraderpanel.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.model.manufacterer.Data;

import java.util.ArrayList;

public class ManufacturerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Data> mListData;
    private LayoutInflater mInflater;

    public ManufacturerAdapter(Context context, ArrayList<Data> mListData) {
        this.context = context;
        this.mListData = mListData;
        mInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.item_list, null);
        AppCompatTextView names = view.findViewById(R.id.txtName);
        Data mData = mListData.get(i);
        names.setText(mData.getName());
        return view;
    }
}
