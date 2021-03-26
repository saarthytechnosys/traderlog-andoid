package com.bibsindia.bibstraderpanel.activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.adapter.ConfirmShadesAdapter;
import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.ActivityConfirmOrderBinding;
import com.bibsindia.bibstraderpanel.databinding.DialogOrderSuccessBinding;
import com.bibsindia.bibstraderpanel.model.catalogue.ShadeDetail;
import com.bibsindia.bibstraderpanel.model.placeOrder.PlaceOrder;
import com.bibsindia.bibstraderpanel.model.placeOrder.Shade;
import com.bibsindia.bibstraderpanel.utils.NetworkUtils;
import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.preference.PowerPreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bibsindia.bibstraderpanel.TraderPanel.TOKEN;
import static com.bibsindia.bibstraderpanel.utils.Utils.HideLoading;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowAlert;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowLoading;

public class ConfirmOrderActivity extends AppCompatActivity {

    private static final String TAG = "CatalogueDetailsActivity";
    private ActivityConfirmOrderBinding mBinding;
    private Activity mActivity;
    private KProgressHUD mLoading;
    public static ArrayList<ShadeDetail> mListData = new ArrayList<>();
    public static int mCatalogueId = -1;
    public static int m_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityConfirmOrderBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mActivity = ConfirmOrderActivity.this;

        mBinding.txtTitle.setSelected(true);

        //Intent intent = getIntent();
        if (mListData.size() != 0) {
            mBinding.layoutNoData.setVisibility(View.GONE);
            mBinding.scrollView.setVisibility(View.VISIBLE);
            //mListData = (ArrayList<ShadeDetail>) getIntent().getSerializableExtra("shadesDetails");

            mBinding.rvShades.setLayoutManager(new LinearLayoutManager(ConfirmOrderActivity.this, RecyclerView.VERTICAL, false));
            ConfirmShadesAdapter mAdapter = new ConfirmShadesAdapter(mActivity, mListData, mShadeDetail -> {
                ShowPreviewShades(mShadeDetail);
            });
            mBinding.rvShades.setAdapter(mAdapter);
        }
    }

    public void clickBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void OrderNow(View view) {
        if (mListData.size() != 0) {
            PlaceOrder mPlaceOrder = new PlaceOrder();
            List<Shade> mListShade = new ArrayList<>();
            for (ShadeDetail mShadeDetail : mListData) {
                mListShade.add(new Shade(mShadeDetail.getTotalQty(), mShadeDetail.getShadeId()));
            }
            mPlaceOrder.setShade(mListShade);
            if (NetworkUtils.isConnected(mActivity)) {
                mLoading = ShowLoading(mActivity);
                PlaceOrder(mPlaceOrder);
            }
        } else
            Log.e("OrderNow", "mListData is Empty!");
    }

    private void PlaceOrder(PlaceOrder mPlaceOrder) {
        Call<ResponseBody> call = Client.getInstance().getApi().PlacesOrder("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN), mCatalogueId, m_id, mPlaceOrder);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "PlaceOrder Response =>" + res);

                        Dialog mAlertDialog = new Dialog(ConfirmOrderActivity.this);
                        mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        DialogOrderSuccessBinding mAlertBinding = DialogOrderSuccessBinding.inflate(getLayoutInflater());
                        mAlertDialog.setContentView(mAlertBinding.getRoot());
                        Objects.requireNonNull(mAlertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
                        mAlertDialog.setCancelable(false);
                        mAlertDialog.setCanceledOnTouchOutside(false);
                        mAlertDialog.show();

                        JSONObject mObj = new JSONObject(res);
                        mObj = mObj.getJSONObject("data");
                        String orderNo = mObj.getString("order_no");
                        mAlertBinding.txtMsg.setText(String.format(getString(R.string.thank_you_desc), orderNo));

                        mAlertBinding.btnOkay.setOnClickListener(v -> {
                            mAlertDialog.dismiss();
                            mListData.clear();
                            setResult(RESULT_OK);
                            finish();
                        });

                    } else if (response.code() == 500) {
                        ShowAlert(mActivity, null, "Internal Server Error", false);
                    } else {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            ShowAlert(mActivity, null, key + " : " + mObj.getString(key), false);
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

    private void ShowPreviewShades(ShadeDetail mShadeDetail) {
        Dialog mPreviewDialog = new Dialog(mActivity);
        mPreviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        com.bibsindia.bibstraderpanel.databinding.DialogShadeImgBinding mPreviewBinding = com.bibsindia.bibstraderpanel.databinding.DialogShadeImgBinding.inflate(mActivity.getLayoutInflater());
        mPreviewDialog.setContentView(mPreviewBinding.getRoot());
        Objects.requireNonNull(mPreviewDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        mPreviewDialog.setCancelable(true);
        mPreviewDialog.setCanceledOnTouchOutside(false);
        mPreviewDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPreviewDialog.show();

        Glide.with(mActivity).load(mShadeDetail.getShadeThumbImage()).into(mPreviewBinding.photoView);

        mPreviewBinding.imgClose.setOnClickListener(v -> {
            mPreviewDialog.dismiss();
        });
    }
}