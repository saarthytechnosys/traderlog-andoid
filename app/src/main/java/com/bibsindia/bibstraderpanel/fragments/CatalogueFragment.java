package com.bibsindia.bibstraderpanel.fragments;

import android.app.Dialog;
import android.content.Context;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bibsindia.bibstraderpanel.activity.CatalogueDetailsActivity;
import com.bibsindia.bibstraderpanel.adapter.CataloguesAdapter;
import com.bibsindia.bibstraderpanel.adapter.ListAdapter;
import com.bibsindia.bibstraderpanel.adapter.ManufacturerAdapter;
import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.DialogCatalogueFilterBinding;
import com.bibsindia.bibstraderpanel.databinding.FragmentCatalogueBinding;
import com.bibsindia.bibstraderpanel.model.catalogue.CatalogueList;
import com.bibsindia.bibstraderpanel.model.list.Data;
import com.bibsindia.bibstraderpanel.model.list.List;
import com.bibsindia.bibstraderpanel.model.manufacterer.Manufacturer;
import com.bibsindia.bibstraderpanel.utils.NetworkUtils;
import com.bibsindia.bibstraderpanel.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.preference.PowerPreference;

import org.json.JSONObject;

import java.util.ArrayList;
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

public class CatalogueFragment extends Fragment {

    //private Activity mActivity;
    private Context mActivity;
    private FragmentCatalogueBinding mBinding;
    private KProgressHUD mLoading;

    private Dialog mFilterDialog;

    private String mCatalogueName = "";
    private String mCatalogueNo = "";
    private String mProductId = "";
    private String mFabricId = "";
    private String mVariantId = "";
    private int mManufacturerId = -1;

    private int mProductPos = 0;
    private int mFabricPos = 0;
    private int mVariantPos = 0;
    private int mManufacturerPos = 0;

    private CataloguesAdapter mAdapter;
    private boolean isLoading;
    private boolean isLastPage = false;
    private int LIMIT = 20;
    private int OFFSET = 0;
    private boolean isShared;
    private boolean isApplyVariant = true;

    private ArrayList<Data> mProductList = new ArrayList<>();
    private ArrayList<Data> mFabricList = new ArrayList<>();
    private ArrayList<Data> mVariantList = new ArrayList<>();
    private ArrayList<com.bibsindia.bibstraderpanel.model.manufacterer.Data> mManufacturerList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentCatalogueBinding.inflate(inflater, container, false);
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            isShared = mBundle.getBoolean("isShared");
        }

        Data mProduct = new Data();
        mProduct.setId(-1);
        mProduct.setName("Select Product");
        mProductList.add(mProduct);

        Data mFabric = new Data();
        mFabric.setId(-1);
        mFabric.setName("Select Fabric");
        mFabricList.add(mFabric);

        Data mVariant = new Data();
        mVariant.setId(-1);
        mVariant.setName("Select Variant");
        mVariantList.add(mVariant);

        Log.w("**********", "OnCreate...............");

        if (NetworkUtils.isConnected(getActivity())) {
            mLoading = ShowLoading(getActivity());
            GetManufactureData();
        }

        mBinding.floatingFilter.setOnClickListener(v -> ShowFilterDialog());

        return mBinding.getRoot();
    }

    private void InitData() {
        isLastPage = false;
        OFFSET = 0;
        GridLayoutManager mLayoutManager = new GridLayoutManager(mActivity, 2);
        mBinding.rvCatalogue.setLayoutManager(mLayoutManager);
        mAdapter = new CataloguesAdapter(getActivity(), isShared);
        mBinding.rvCatalogue.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(mData -> {
            Log.w("CatalogueAdapter", "Clicked!");
            mData.setManufacturerId(mManufacturerId);
            mData.setManufacturerName(mManufacturerList.get(mManufacturerPos).getName());
            Intent i = new Intent(mActivity, CatalogueDetailsActivity.class);
            i.putExtra("mCatalogueDetails", mData);
            i.putExtra("isShared", isShared);
            startActivity(i);
        });
        mBinding.rvCatalogue.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        Log.w("Pagination", "Condition In");
                        isLoading = true;
                        //indexPage += 1;
                        OFFSET = OFFSET + LIMIT;
                        new Handler().postDelayed(() -> CatalogueList(false), 1000);
                    }
                }
            }
        });

        if (NetworkUtils.isConnected(getActivity())) {
            mLoading = ShowLoading(getActivity());
            CatalogueList(true);
        }
    }

    private void CatalogueList(boolean isFirstTime) {
        Call<ResponseBody> call;
        if (isShared)
            call = Client.getInstance().getApi()
                    .CataloguesListShare("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN),
                            mManufacturerId, LIMIT, OFFSET, mCatalogueName, mCatalogueNo, mProductId, mFabricId, mVariantId);
        else
            call = Client.getInstance().getApi()
                    .CataloguesList("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN),
                            mManufacturerId, LIMIT, OFFSET, mCatalogueName, mCatalogueNo, mProductId, mFabricId, mVariantId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.code() == 200) {
                        String res = response.body().string();
                        Log.e("CatalogueList", "CatalogueList Response =>" + res);

                        Gson gson = new GsonBuilder().create();
                        CatalogueList mCatalogueList = gson.fromJson(res, CatalogueList.class);
                        mBinding.txtNoData.setVisibility(View.GONE);
                        mBinding.rvCatalogue.setVisibility(View.VISIBLE);

                        Log.e("TAG", "Total Orders =>" + mCatalogueList.getData().size());
                        Log.e("TAG", "Total Orders =>" + mAdapter.getItemCount());

                        if (isFirstTime) {
                            mAdapter.addAll(mCatalogueList.getData());
                            if (mCatalogueList.getData().size() >= LIMIT) {
                                mAdapter.addLoadingFooter();
                            } else {
                                //  mAdapter.removeLoadingFooter();
                                isLoading = false;
                                isLastPage = true;
                            }
                        } else {
                            mAdapter.removeLoadingFooter();
                            isLoading = false;

                            mAdapter.addAll(mCatalogueList.getData());
                            if (mCatalogueList.getData().size() >= LIMIT) {
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
                            mBinding.rvCatalogue.setVisibility(View.GONE);
                        } else {
                            mAdapter.removeLoadingFooter();
                            isLoading = false;
                            isLastPage = true;
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
        DialogCatalogueFilterBinding mFilterBinding = DialogCatalogueFilterBinding.inflate(getActivity().getLayoutInflater());
        mFilterDialog.setContentView(mFilterBinding.getRoot());
        Objects.requireNonNull(mFilterDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        mFilterDialog.setCancelable(true);
        mFilterDialog.setCanceledOnTouchOutside(true);
        mFilterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mFilterDialog.show();

        if (!mCatalogueName.isEmpty())
            mFilterBinding.edCatalogueName.setText(mCatalogueName);

        if (!mCatalogueNo.isEmpty())
            mFilterBinding.edCatalogueNumber.setText(mCatalogueNo);

        FillProduct(mFilterBinding);
        isApplyVariant = mFabricPos == 0;
        FillFabric(mFilterBinding);

        FillVariant(mFilterBinding);

        FillManufacturer(mFilterBinding);

        mFilterBinding.spinnerProductId.setSelection(mProductPos);
        mFilterBinding.spinnerFabricId.setSelection(mFabricPos);
        mFilterBinding.spinnerSubFabricId.setSelection(mVariantPos);
        mFilterBinding.spinnerManufacturerId.setSelection(mManufacturerPos);

        mFilterBinding.btnFind.setOnClickListener(v -> {
            String cataName = String.valueOf(mFilterBinding.edCatalogueName.getText());
            if (!cataName.isEmpty()) mCatalogueName = cataName;
            else mCatalogueName = "";

            String cataNo = String.valueOf(mFilterBinding.edCatalogueNumber.getText());
            if (!cataNo.isEmpty()) mCatalogueNo = cataNo;
            else mCatalogueNo = "";

            mFilterDialog.dismiss();

            InitData();
        });

        mFilterBinding.btnClear.setOnClickListener(v -> {
            mCatalogueName = "";
            mCatalogueNo = "";
            mProductId = "";
            mFabricId = "";
            mVariantId = "";

            mProductPos = 0;
            mFabricPos = 0;
            mVariantPos = 0;

            mFilterDialog.dismiss();

            InitData();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    private void GetProductData() {
        Call<ResponseBody> call = Client.getInstance().getApi().ProductList("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN), mManufacturerId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "GetProductData Response =>" + res);

                        Gson gson = new GsonBuilder().create();
                        List mList = gson.fromJson(res, List.class);

                        if (mList.getSuccess()) {
                            mProductList.addAll(mList.getData());
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

    private void GetFabricData() {
        Call<ResponseBody> call = Client.getInstance().getApi().FabricList("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN), mManufacturerId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "GetFabricData Response =>" + res);

                        Gson gson = new GsonBuilder().create();
                        List mList = gson.fromJson(res, List.class);

                        if (mList.getSuccess()) {
                            mFabricList.addAll(mList.getData());
                            Log.w("**********", "GetFabricData...............");
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

    private void GetVariantData(DialogCatalogueFilterBinding mFilterBinding, int fabric_id) {

        Call<ResponseBody> call = Client.getInstance().getApi().VariantList("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN), mManufacturerId, fabric_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "GetVariantData Response =>" + res);

                        Gson gson = new GsonBuilder().create();
                        List mList = gson.fromJson(res, List.class);

                        mVariantList.clear();
                        Data mVariant = new Data();
                        mVariant.setId(-1);
                        mVariant.setName("Select Variant");
                        mVariantList.add(mVariant);
                        if (mList.getSuccess()) {
                            mVariantList.addAll(mList.getData());
                            Log.w("**********", "mVariantList...............");
                            FillVariant(mFilterBinding);
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

    private void GetManufactureData() {
        Call<ResponseBody> call = Client.getInstance().getApi().ManufacturerList("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "GetProductData Response =>" + res);

                        Gson gson = new GsonBuilder().create();
                        Manufacturer mManufacturer = gson.fromJson(res, Manufacturer.class);

                        mManufacturerList.clear();
                        if (mManufacturer.getSuccess()) {
                            mManufacturerList.addAll(mManufacturer.getData());
                            Log.w("**********", "GetProductData...............");

                            mManufacturerId = mManufacturerList.get(mManufacturerPos).getId();
                            if (NetworkUtils.isConnected(getActivity())) {
                                GetProductData();
                                GetFabricData();
                                InitData();
                            }

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
                HideLoading(mLoading);
            }
        });
    }

    private void FillProduct(DialogCatalogueFilterBinding mFilterBinding) {
        Log.w("DataList", "FillProduct : " + mProductList.size());
        ListAdapter mListAdapter = new ListAdapter(mActivity, mProductList);
        mFilterBinding.spinnerProductId.setAdapter(mListAdapter);
        mFilterBinding.spinnerProductId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w("DataList", "Product Position: " + position + "\t\t" + mProductList.get(position).getName() + "\t\tid : " + id);
                mProductPos = position;
                if (position == 0)
                    mProductId = "";
                else
                    mProductId = String.valueOf(mProductList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void FillFabric(DialogCatalogueFilterBinding mFilterBinding) {
        Log.w("DataList", "FillFabric : " + mFabricList.size());
        ListAdapter mListAdapter = new ListAdapter(mActivity, mFabricList);
        mFilterBinding.spinnerFabricId.setAdapter(mListAdapter);
        mFilterBinding.spinnerFabricId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w("DataList", "Fabric Position: " + position + "\t\t" + mFabricList.get(position).getName() + "\t\tid : " + id);
                if (isApplyVariant) {
                    mFabricPos = position;
                    if (position == 0)
                        mFabricId = "";
                    else {
                        mFabricId = String.valueOf(mFabricList.get(position).getId());
                        Log.w("DataList", "//////////////////////////");
                        GetVariantData(mFilterBinding, Integer.parseInt(mFabricId));
                    }
                } else
                    isApplyVariant = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }


        });
    }

    private void FillVariant(DialogCatalogueFilterBinding mFilterBinding) {
        Log.w("DataList", "FillVariant : " + mVariantList.size());
        ListAdapter mListAdapter = new ListAdapter(mActivity, mVariantList);
        mFilterBinding.spinnerSubFabricId.setAdapter(mListAdapter);
        mFilterBinding.spinnerSubFabricId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w("DataList", " Variant Position: " + position + "\t\t" + mVariantList.get(position).getName() + "\t\tid : " + id);
                mVariantPos = position;
                if (position == 0)
                    mVariantId = "";
                else
                    mVariantId = String.valueOf(mVariantList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void FillManufacturer(DialogCatalogueFilterBinding mFilterBinding) {
        Log.w("DataList", "FillManufacturer : " + mManufacturerList.size());
        ManufacturerAdapter mManufacturerAdapter = new ManufacturerAdapter(mActivity, mManufacturerList);
        mFilterBinding.spinnerManufacturerId.setAdapter(mManufacturerAdapter);
        mFilterBinding.spinnerManufacturerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w("DataList", "Manufacturer Position: " + position + "\t\t" + mManufacturerList.get(position).getName() + "\t\tid : " + id);
                mManufacturerPos = position;
                mManufacturerId = mManufacturerList.get(mManufacturerPos).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
}