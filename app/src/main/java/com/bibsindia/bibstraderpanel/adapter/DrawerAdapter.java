package com.bibsindia.bibstraderpanel.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.model.MenuModel;

import java.util.HashMap;
import java.util.List;

public class DrawerAdapter extends BaseExpandableListAdapter {
    private Activity mActivity;
    private List<MenuModel> listDataHeader;
    private HashMap<MenuModel, List<MenuModel>> listDataChild;
    private onItemClickListener mListener;
    private int mSelectedGroupPos = 0;
    private View mSelectedChild = null;

    public DrawerAdapter(Activity mActivity, List<MenuModel> listDataHeader,
                         HashMap<MenuModel, List<MenuModel>> listChildData, onItemClickListener mListener) {
        this.mActivity = mActivity;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.mListener = mListener;
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MenuModel menuModel = getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_drawable_sub, null);
        }
        LinearLayout rootChild = convertView.findViewById(R.id.rootChild);
        AppCompatTextView txtTraderName = convertView.findViewById(R.id.txtTraderName);
        txtTraderName.setSelected(true);
        String name = menuModel.menuName;
        Log.w("Manufacturer", "Name : " + name);
        if (name.equalsIgnoreCase("NO")) {
            txtTraderName.setText(mActivity.getString(R.string.no_manufacturer));
            txtTraderName.setTextColor(ContextCompat.getColor(mActivity, R.color.colorDark));
        } else {
            txtTraderName.setText(name);
            txtTraderName.setTextColor(ContextCompat.getColor(mActivity, R.color.colorBlack));
        }

        rootChild.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimaryTrans));

        View finalConvertView = convertView;
        rootChild.setOnClickListener(v -> {
            mListener.onChildClick(groupPosition, childPosition, finalConvertView, mSelectedChild, menuModel);
            mSelectedChild = finalConvertView;
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        int headerIcon = getGroup(groupPosition).menuIcon;
        String headerTitle = getGroup(groupPosition).menuName;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_drawable, null);
        }

        LinearLayout groupRoot = convertView.findViewById(R.id.groupRoot);
        AppCompatImageView imgMenu = convertView.findViewById(R.id.imgMenu);
        AppCompatTextView txtName = convertView.findViewById(R.id.txtName);
        AppCompatImageView imgToggle = convertView.findViewById(R.id.imgToggle);
        txtName.setSelected(true);
        txtName.setText(headerTitle);
        imgMenu.setImageResource(headerIcon);

        if (getChildrenCount(groupPosition) == 0)
            imgToggle.setVisibility(View.INVISIBLE);
        else
            imgToggle.setVisibility(View.VISIBLE);

        if (mSelectedGroupPos == groupPosition) {
            imgToggle.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            groupRoot.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
            imgMenu.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorAccent));
            txtName.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
            imgToggle.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorAccent));
        } else {
            imgToggle.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            groupRoot.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
            imgMenu.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary));
            txtName.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
            imgToggle.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary));
        }

        groupRoot.setOnClickListener(v -> {
            mListener.onGroupClick(groupPosition, mSelectedGroupPos);
            mSelectedGroupPos = groupPosition;
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface onItemClickListener {
        void onGroupClick(int groupPositionNew, int groupPositionOld);

        void onChildClick(int groupPosition, int childPosition, View childNew, View childOld, MenuModel model);
    }
}
