package com.bibsindia.bibstraderpanel.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.activity.OrderDetailsActivity;
import com.bibsindia.bibstraderpanel.adapter.ManufacturerAdapter;
import com.bibsindia.bibstraderpanel.adapter.OrderAdapter;
import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.DialogOrderFilterBinding;
import com.bibsindia.bibstraderpanel.databinding.FragmentOrderHistoryBinding;
import com.bibsindia.bibstraderpanel.model.manufacterer.Data;
import com.bibsindia.bibstraderpanel.model.manufacterer.Manufacturer;
import com.bibsindia.bibstraderpanel.model.order.History;
import com.bibsindia.bibstraderpanel.utils.NetworkUtils;
import com.bibsindia.bibstraderpanel.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.preference.PowerPreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bibsindia.bibstraderpanel.TraderPanel.TOKEN;
import static com.bibsindia.bibstraderpanel.utils.Utils.HideLoading;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowAlert;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowLoading;

public class OrderHistoryFragment extends Fragment {

    private Activity mActivity;
    private FragmentOrderHistoryBinding mBinding;
    private KProgressHUD mLoading;
    private String mSelectedDateFrom;
    private String mSelectedDateTo;
    private String mOrderNumber = "";
    private Dialog mFilterDialog;
    private int toDay, toMonth, toYear;
    private int fromDay, fromMonth, fromYear;
    private ArrayList<Data> mManufacturerList = new ArrayList<>();
    private OrderAdapter mAdapter;
    private boolean isLoading;
    private boolean isLastPage = false;
    private int LIMIT = 20;
    private int OFFSET = 0;
    private int mManufacturerPos = 0;
    private int mManufacturerId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentOrderHistoryBinding.inflate(inflater, container, false);
        mActivity = getActivity();

        Reset();

        mBinding.floatingFilter.setOnClickListener(v -> ShowFilterDialog());

        if (NetworkUtils.isConnected(getActivity())) {
            GetManufactureData();
        }

        InitData();

        return mBinding.getRoot();
    }

    private void Reset() {
        Calendar fromDate = Calendar.getInstance();
        fromDate.add(Calendar.MONTH, -1);
        fromYear = fromDate.get(Calendar.YEAR);
        fromMonth = fromDate.get(Calendar.MONTH);
        fromDay = fromDate.get(Calendar.DAY_OF_MONTH);
        mSelectedDateFrom = String.format("%d-%d-%d", fromYear, fromMonth + 1, fromDay);
        Log.w("Date", "FromDate : " + mSelectedDateFrom);

        Calendar toDate = Calendar.getInstance();
        toYear = toDate.get(Calendar.YEAR);
        toMonth = toDate.get(Calendar.MONTH);
        toDay = toDate.get(Calendar.DAY_OF_MONTH);
        mSelectedDateTo = String.format("%d-%d-%d", toYear, toMonth + 1, toDay);
        Log.w("Date", "ToDate : " + mSelectedDateTo);

        mOrderNumber = "";
        mManufacturerId = -1;
        mManufacturerPos = 0;
    }

    private void InitData() {
        isLastPage = false;
        OFFSET = 0;
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false);
        mBinding.rvOrderHistory.setLayoutManager(mLayoutManager);
        mAdapter = new OrderAdapter(mActivity);
        mBinding.rvOrderHistory.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(mData -> {
            Intent i = new Intent(mActivity, OrderDetailsActivity.class);
            i.putExtra("mOrderNo", mData.getOrderNo());
            startActivity(i);
        });
        mBinding.rvOrderHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mBinding.floatingFilter.getVisibility() == View.VISIBLE) {
                    mBinding.floatingFilter.hide();
                } else if (dy < 0 && mBinding.floatingFilter.getVisibility() != View.VISIBLE) {
                    mBinding.floatingFilter.show();
                }

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();

                int firstVisibleItemPosition;
                if (visibleItemCount > 0) {
                    firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                } else {
                    firstVisibleItemPosition = 0;
                }

                Log.w("Pagination", "isLoading : " + isLoading + "\tisLastPage:" + isLastPage);
                if (!isLoading && !isLastPage) {
                    Log.w("Pagination", "visibleItemCount : " + visibleItemCount + "\tfirstVisibleItemPosition:" + firstVisibleItemPosition + "\ttotalItemCount" + totalItemCount);
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 /*&& totalItemCount >= TOTAL_PAGE*/) {
                        Log.w("Pagination", "Condition In");
                        isLoading = true;
                        //indexPage += 1;
                        OFFSET = OFFSET + LIMIT;
                        new Handler().postDelayed(() -> OrderHistory(false), 1000);
                    }
                }
            }
        });

        if (NetworkUtils.isConnected(mActivity)) {
            mLoading = ShowLoading(mActivity);
            OrderHistory(true);
        }
    }

    private void OrderHistory(boolean isFirstTime) {
        Call<ResponseBody> call = Client.getInstance().getApi()
                .OrderHistory("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN),
                        LIMIT, OFFSET, mOrderNumber,
                        mSelectedDateFrom, mSelectedDateTo, (mManufacturerId == -1) ? "" : String.valueOf(mManufacturerId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.code() == 200) {
                        String res = response.body().string();
                        Log.e("TAG", "Dashboards Response =>" + res);

                        Gson gson = new GsonBuilder().create();
                        History mHistory = gson.fromJson(res, History.class);
                        mBinding.txtNoData.setVisibility(View.GONE);
                        mBinding.rvOrderHistory.setVisibility(View.VISIBLE);

                        Log.e("TAG", "Total Orders =>" + mHistory.getData().size());

                        if (isFirstTime) {
                            mAdapter.addAll(mHistory.getData());
                            if (mHistory.getData().size() >= LIMIT) {
                                mAdapter.addLoadingFooter();
                            } else {
                                //  mAdapter.removeLoadingFooter();
                                isLoading = false;
                                isLastPage = true;
                            }
                        } else {
                            mAdapter.removeLoadingFooter();
                            isLoading = false;

                            mAdapter.addAll(mHistory.getData());
                            if (mHistory.getData().size() >= LIMIT) {
                                mAdapter.addLoadingFooter();
                            } else {
                                mAdapter.removeLoadingFooter();
                                isLoading = false;
                                isLastPage = true;
                            }
                        }
                    } else if (response.code() == 204) {
                        if (isFirstTime) {
                            mBinding.txtNoData.setVisibility(View.VISIBLE);
                            mBinding.rvOrderHistory.setVisibility(View.GONE);
                        } else {
                            mAdapter.removeLoadingFooter();
                            isLoading = false;
                            isLastPage = true;
                        }
                    } else if (response.code() == 500) {
                        ShowAlert(mActivity, null, "Internal Server Error", false);
                    } else if (response.code() == 401) {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            Utils.ShowAlertLogout(getActivity(), null, mObj.getString(key));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            ShowAlert(mActivity, null, mObj.getString(key), false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    HideLoading(mLoading);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                HideLoading(mLoading);
            }
        });
    }

    private void ShowFilterDialog() {
        mFilterDialog = new Dialog(mActivity);
        mFilterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogOrderFilterBinding mFilterBinding = DialogOrderFilterBinding.inflate(mActivity.getLayoutInflater());
        mFilterDialog.setContentView(mFilterBinding.getRoot());
        Objects.requireNonNull(mFilterDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        mFilterDialog.setCancelable(true);
        mFilterDialog.setCanceledOnTouchOutside(true);
        mFilterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mFilterDialog.show();

        FillManufacturer(mFilterBinding);

        mFilterBinding.spinnerManufacturerId.setSelection(mManufacturerPos);
        mFilterBinding.txtFromData.setText(mSelectedDateFrom);
        mFilterBinding.txtToData.setText(mSelectedDateTo);

        mFilterBinding.txtFromData.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.AppCompatDatePickerDialogStyle,
                    (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
                        fromYear = year;
                        fromMonth = monthOfYear + 1;
                        fromDay = dayOfMonth;
                        mFilterBinding.txtFromData.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }, fromYear, fromMonth, fromDay);
            datePickerDialog.show();
        });

        mFilterBinding.txtToData.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.AppCompatDatePickerDialogStyle,
                    (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
                        toYear = year;
                        toMonth = monthOfYear + 1;
                        toDay = dayOfMonth;
                        mFilterBinding.txtToData.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }, toYear, toMonth, toDay);
            datePickerDialog.show();
        });

        mFilterBinding.btnFind.setOnClickListener(v -> {
            mSelectedDateFrom = String.valueOf(mFilterBinding.txtFromData.getText());
            mSelectedDateTo = String.valueOf(mFilterBinding.txtToData.getText());
            String orderNo = String.valueOf(mFilterBinding.edOrderNumber.getText());
            if (!orderNo.isEmpty())
                mOrderNumber = orderNo;
            else
                mOrderNumber = "";

            mFilterDialog.dismiss();

            InitData();
        });

        mFilterBinding.btnClear.setOnClickListener(v -> {
            Reset();

            mFilterDialog.dismiss();

            InitData();
        });

    }

    private void GetManufactureData() {
        Call<ResponseBody> call = Client.getInstance().getApi().ManufacturerList("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "GetProductData Response =>" + res);

                        Gson gson = new GsonBuilder().create();
                        Manufacturer mManufacturer = gson.fromJson(res, Manufacturer.class);

                        mManufacturerList.clear();
                        Data mMgf = new Data();
                        mMgf.setId(-1);
                        mMgf.setName("Select Manufacturer");
                        mManufacturerList.add(mMgf);

                        if (mManufacturer.getSuccess()) {
                            mManufacturerList.addAll(mManufacturer.getData());
                            Log.w("**********", "GetProductData...............");
                        }
                    } else if (response.code() == 500) {
                        ShowAlert(getActivity(), null, "Internal Server Error", false);
                    } else if (response.code() == 401) {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            Utils.ShowAlertLogout(getActivity(), null, mObj.getString(key));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            ShowAlert(getActivity(), null, mObj.getString(key), false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
        });
    }

    private void FillManufacturer(DialogOrderFilterBinding mFilterBinding) {
        Log.w("DataList", "FillManufacturer : " + mManufacturerList.size());
        ManufacturerAdapter mManufacturerAdapter = new ManufacturerAdapter(mActivity, mManufacturerList);
        mFilterBinding.spinnerManufacturerId.setAdapter(mManufacturerAdapter);
        mFilterBinding.spinnerManufacturerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w("DataList", "Manufacturer Position: " + position + "\t\t" + mManufacturerList.get(position).getName() + "\t\tid : " + id);
                mManufacturerPos = position;
                if (position == 0)
                    mManufacturerId = -1;
                else
                    mManufacturerId = mManufacturerList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}