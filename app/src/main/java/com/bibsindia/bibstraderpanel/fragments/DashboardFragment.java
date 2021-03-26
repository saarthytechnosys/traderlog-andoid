package com.bibsindia.bibstraderpanel.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.activity.HomeScreenActivity;
import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.FragmentDashboardBinding;
import com.bibsindia.bibstraderpanel.model.dashboard.Dashboard;
import com.bibsindia.bibstraderpanel.utils.NetworkUtils;
import com.bibsindia.bibstraderpanel.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.preference.PowerPreference;

import org.json.JSONObject;

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

public class DashboardFragment extends Fragment {

    private Activity mActivity;
    private FragmentDashboardBinding mBinding;
    private KProgressHUD mLoading;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentDashboardBinding.inflate(inflater, container, false);
        mActivity = getActivity();
        if (NetworkUtils.isConnected(mActivity)) {
            mLoading = ShowLoading(mActivity);
            Dashboards();
        }

       /* mBinding.layoutLatestCatalogue.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isShared", true);
            CatalogueFragment mCatalogueFragment = new CatalogueFragment();
            mCatalogueFragment.setArguments(bundle);

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, mCatalogueFragment);
            getActivity().setTitle(getString(R.string.catalogue_share));
            Objects.requireNonNull(((HomeScreenActivity) getActivity()).getSupportActionBar()).setTitle(getString(R.string.catalogue_share));
            transaction.commit();
        });*/
        return mBinding.getRoot();
    }

    private void Dashboards() {
        Log.e("TAG", "Dashboards Response =>");
        Call<ResponseBody> call = Client.getInstance().getApi().Dashboards("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "Dashboards Response =>" + res);

                        Gson gson = new GsonBuilder().create();
                        Dashboard mDashboard = gson.fromJson(res, Dashboard.class);

                        mBinding.txtTotalPending.setText(String.valueOf(mDashboard.getData().getPendingOrderCount()));
                        mBinding.txtTotalApproved.setText(String.valueOf(mDashboard.getData().getApprovedOrderCount()));
                        mBinding.txtTotalOrders.setText(String.valueOf(mDashboard.getData().getTotalOrderCount()));

                        mBinding.txtTodayOrders.setText(String.valueOf(mDashboard.getData().getTodayOrderCount()));

                        mBinding.txtLatestCatalogues.setText(String.valueOf(mDashboard.getData().getLastSevendayCatalogueCount()));

                        mBinding.txtTotalCatalogues.setText(String.valueOf(mDashboard.getData().getTotalCatalogueCount()));

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
                            ShowAlert(mActivity, null,  mObj.getString(key), false);
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
}